package com.tonyp.dictionarykotlin.meaning.app.v1

import co.touchlab.stately.collections.ConcurrentMutableMap
import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.asDictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.helpers.asDictionaryExtension
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow

private val sessions = ConcurrentMutableMap<WebSocketSession, DictionaryContext>()

suspend fun WebSocketSession.searchMeaning() {
    sessions[this] = DictionaryContext()

    incoming.receiveAsFlow().mapNotNull{
        val frame = it as? Frame.Text ?: return@mapNotNull
        val context = DictionaryContext()
        sessions[this] = context
        try {
            val request = apiV1Mapper.readValue(frame.readText(), MeaningSearchRequest::class.java)
            context.fromTransport(request)
            context.command = DictionaryCommand.SEARCH_INIT
            context.webSocketExtensions.addAll(extensions.map{ e -> e.asDictionaryExtension()}.toList())
            context.meaningsResponse.addAll(DictionaryMeaningStub.getSearchResult())
            send(apiV1Mapper.writeValueAsString(context.toTransportMeaning()))
        } catch (_: ClosedReceiveChannelException) {
            sessions.clear()
        } catch (e: Exception) {
            context.command = DictionaryCommand.SEARCH_INIT
            context.errors.add(e.asDictionaryError())
            send(apiV1Mapper.writeValueAsString(context.toTransportMeaning()))
        }
    }.collect()
}