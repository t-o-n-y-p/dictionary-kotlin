package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.helpers.createInitContext
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.*
import kotlin.collections.HashMap

private val sessions = Collections.synchronizedMap<WebSocketSession, DictionaryContext>(HashMap())

suspend fun WebSocketSession.searchMeaning() {
    val initContext = createInitContext()
    sessions[this] = initContext
    trySendResponse(initContext)

    incoming.receiveAsFlow().mapNotNull{
        val frame = it as? Frame.Text ?: return@mapNotNull
        try {
            val context = DictionaryContext()
            val request = apiV1Mapper.readValue(frame.readText(), MeaningSearchRequest::class.java)
            context.fromTransport(request)
            context.meaningsResponse.addAll(DictionaryMeaningStub.getSearchResult())
            trySendResponse(context)
            sessions[this] = context
        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (e: Exception) {
            trySendResponse(createInitContext(e))
        }
    }.collect()
}

suspend fun sendWebSocketCreateDeleteNotification(context: DictionaryContext) =
    sessions.entries
        .filter {
            (
                    it.value.meaningFilterRequest.word == context.meaningResponse.word && (
                            it.value.meaningFilterRequest.approved == DictionaryMeaningApproved.NONE
                                    || it.value.meaningFilterRequest.approved == context.meaningResponse.approved
                            )
            ) || (
                    it.value.meaningFilterRequest.word == ""
                            && it.value.meaningFilterRequest.approved == context.meaningResponse.approved
                    )
        }.forEach {
            it.key.trySendResponse(context)
        }

suspend fun sendWebSocketUpdateNotification(context: DictionaryContext) {
    sessions.entries
        .filter {
            it.value.meaningFilterRequest.word == context.meaningResponse.word
                    && it.value.meaningFilterRequest.approved == DictionaryMeaningApproved.NONE
        }.forEach {
            it.key.trySendResponse(context)
        }
    sessions.entries
        .filter {
            (it.value.meaningFilterRequest.word == context.meaningResponse.word || it.value.meaningFilterRequest.word == "")
                    && it.value.meaningFilterRequest.approved == context.meaningResponse.approved
        }.forEach {
            context.command = DictionaryCommand.CREATE
            it.key.trySendResponse(context)
        }
    sessions.entries
        .filter {
            (it.value.meaningFilterRequest.word == context.meaningResponse.word || it.value.meaningFilterRequest.word == "")
                    && it.value.meaningFilterRequest.approved != DictionaryMeaningApproved.NONE
                    && it.value.meaningFilterRequest.approved != context.meaningResponse.approved
        }.forEach {
            context.command = DictionaryCommand.DELETE
            it.key.trySendResponse(context)
        }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun WebSocketSession.trySendResponse(context: DictionaryContext) {
    try {
        send(apiV1Mapper.writeValueAsString(context.toTransportMeaning()))
    } catch (_: Exception) {
        if (outgoing.isClosedForSend) {
            sessions.remove(this)
        }
    }
}
