package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.helpers.WebSocketSessionMap
import com.tonyp.dictionarykotlin.meaning.app.helpers.createInitContext
import com.tonyp.dictionarykotlin.meaning.app.helpers.process
import com.tonyp.dictionarykotlin.meaning.app.helpers.sendResponse
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow

private val sessions = WebSocketSessionMap()

suspend fun WebSocketSession.searchMeaning(appSettings: DictionaryAppSettings) {
    val logId = "search"
    val logger = appSettings.corSettings.loggerProvider.logger(WebSocketSession::searchMeaning)
    logger.doWithLogging(logId) {
        val initContext = createInitContext()
        sessions.put(this, initContext)
        trySendResponse(appSettings, initContext)
    }

    incoming.receiveAsFlow().mapNotNull{
        val frame = it as? Frame.Text ?: return@mapNotNull
        logger.doWithLogging(logId) {
            val context = DictionaryContext()
            val request = apiV1Mapper.readValue(frame.readText(), MeaningSearchRequest::class.java)
            context.fromTransport(request)
            process(appSettings, context)
            trySendResponse(appSettings, context)
            sessions.put(this, context)
        }
    }.catch {
        if (it is ClosedReceiveChannelException) {
            sessions.clear()
        } else {
            trySendResponse(appSettings, createInitContext(it))
        }
    }.collect()
}

suspend fun sendWebSocketCreateDeleteNotification(appSettings: DictionaryAppSettings, context: DictionaryContext) =
    sessions.entries
        .filter {
            (
                    it.value.meaningFilterRequest.word == context.meaningResponse.word
                            || it.value.meaningFilterRequest.word == ""
            ) && (
                    it.value.meaningFilterRequest.approved == DictionaryMeaningApproved.NONE
                            || it.value.meaningFilterRequest.approved == context.meaningResponse.approved
                    )
        }.forEach {
            it.key.trySendResponse(appSettings, context)
        }

suspend fun sendWebSocketUpdateNotification(appSettings: DictionaryAppSettings, context: DictionaryContext) {
    sessions.entries
        .filter {
            (it.value.meaningFilterRequest.word == context.meaningResponse.word || it.value.meaningFilterRequest.word == "")
                    && it.value.meaningFilterRequest.approved == DictionaryMeaningApproved.NONE
        }.forEach {
            it.key.trySendResponse(appSettings, context)
        }
    sessions.entries
        .filter {
            (it.value.meaningFilterRequest.word == context.meaningResponse.word || it.value.meaningFilterRequest.word == "")
                    && it.value.meaningFilterRequest.approved == context.meaningResponse.approved
        }.forEach {
            context.command = DictionaryCommand.CREATE
            it.key.trySendResponse(appSettings, context)
        }
    sessions.entries
        .filter {
            (it.value.meaningFilterRequest.word == context.meaningResponse.word || it.value.meaningFilterRequest.word == "")
                    && it.value.meaningFilterRequest.approved != DictionaryMeaningApproved.NONE
                    && it.value.meaningFilterRequest.approved != context.meaningResponse.approved
        }.forEach {
            context.command = DictionaryCommand.DELETE
            it.key.trySendResponse(appSettings, context)
        }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun WebSocketSession.trySendResponse(appSettings: DictionaryAppSettings, context: DictionaryContext) {
    try {
        sendResponse(appSettings, context)
    } catch (_: Exception) {
        if (outgoing.isClosedForSend) {
            sessions.remove(this)
        }
    }
}
