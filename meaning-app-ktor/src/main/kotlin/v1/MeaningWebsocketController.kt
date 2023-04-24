package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.models.MeaningSearchRequest
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.log.v1.common.IDictionaryLogWrapper
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.helpers.WebSocketSessionMap
import com.tonyp.dictionarykotlin.meaning.app.helpers.createInitContext
import com.tonyp.dictionarykotlin.meaning.app.helpers.sendResponse
import io.ktor.websocket.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach

val sessions = WebSocketSessionMap()

suspend fun WebSocketSession.searchMeaning(appSettings: DictionaryAppSettings, logger: IDictionaryLogWrapper) {
    val initLogId = "websocket-init"
    logger.doWithLogging(initLogId) {
        val initContext = createInitContext()
        sessions.put(this, initContext)
        sendResponse(appSettings, initContext, initLogId)
    }

    processCommand<MeaningSearchRequest>(
        DictionaryCommand.SEARCH, appSettings, logger, "websocket-search"
    )
}

suspend fun sendWebSocketCreateDeleteNotification(appSettings: DictionaryAppSettings, context: DictionaryContext) =
    sessions.entries
        .asFlow()
        .filter {
            it.value.state == DictionaryState.FINISHING &&
            (
                    it.value.meaningFilterValidated.word == context.meaningResponse.word
                            || it.value.meaningFilterValidated.word == ""
            ) && (
                    it.value.meaningFilterValidated.approved == DictionaryMeaningApproved.NONE
                            || it.value.meaningFilterValidated.approved == context.meaningResponse.approved
                    )
        }.onEach {
            it.key.sendResponse(appSettings, context, "websocket-notification")
        }.collect()

suspend fun sendWebSocketUpdateNotification(appSettings: DictionaryAppSettings, context: DictionaryContext) {
    val logId = "websocket-notification"
    sessions.entries
        .asFlow()
        .filter {
            it.value.state == DictionaryState.FINISHING &&
            (it.value.meaningFilterValidated.word == context.meaningResponse.word || it.value.meaningFilterValidated.word == "")
                    && it.value.meaningFilterValidated.approved == DictionaryMeaningApproved.NONE
        }.onEach {
            it.key.sendResponse(appSettings, context, logId)
        }.collect()
    context.command = DictionaryCommand.CREATE
    sessions.entries
        .asFlow()
        .filter {
            it.value.state == DictionaryState.FINISHING &&
            (it.value.meaningFilterValidated.word == context.meaningResponse.word || it.value.meaningFilterValidated.word == "")
                    && it.value.meaningFilterValidated.approved == context.meaningResponse.approved
        }.onEach {
            it.key.sendResponse(appSettings, context, logId)
        }.collect()
    context.command = DictionaryCommand.DELETE
    sessions.entries
        .asFlow()
        .filter {
            it.value.state == DictionaryState.FINISHING &&
            (it.value.meaningFilterValidated.word == context.meaningResponse.word || it.value.meaningFilterValidated.word == "")
                    && it.value.meaningFilterValidated.approved != DictionaryMeaningApproved.NONE
                    && it.value.meaningFilterValidated.approved != context.meaningResponse.approved
        }.onEach {
            it.key.sendResponse(appSettings, context, logId)
        }.collect()
}
