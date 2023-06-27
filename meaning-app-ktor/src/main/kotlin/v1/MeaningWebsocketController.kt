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
    context.takeIf { it.state == DictionaryState.FINISHING }?.let { ctx ->
        sessions.entries
            .filter {
                (
                        it.value.meaningFilterValidated.word == ctx.meaningResponse.word
                                || it.value.meaningFilterValidated.word == ""
                        ) && (
                        it.value.meaningFilterValidated.approved == DictionaryMeaningApproved.NONE
                                || it.value.meaningFilterValidated.approved == ctx.meaningResponse.approved
                        )
            }.forEach {
                it.key.sendResponse(appSettings, ctx, "websocket-notification")
            }
    }

suspend fun sendWebSocketUpdateNotification(appSettings: DictionaryAppSettings, context: DictionaryContext) {
    context.takeIf { it.state == DictionaryState.FINISHING }?.let { ctx ->
        val logId = "websocket-notification"
        sessions.entries
            .filter {
                (it.value.meaningFilterValidated.word == ctx.meaningResponse.word || it.value.meaningFilterValidated.word == "")
                        && it.value.meaningFilterValidated.approved == DictionaryMeaningApproved.NONE
            }.forEach {
                it.key.sendResponse(appSettings, ctx, logId)
            }
        sessions.entries
            .filter {
                (it.value.meaningFilterValidated.word == ctx.meaningResponse.word || it.value.meaningFilterValidated.word == "")
                        && it.value.meaningFilterValidated.approved == ctx.meaningResponse.approved
            }.forEach {
                ctx.command = DictionaryCommand.CREATE
                it.key.sendResponse(appSettings, ctx, logId)
            }
        sessions.entries
            .filter {
                (it.value.meaningFilterValidated.word == ctx.meaningResponse.word || it.value.meaningFilterValidated.word == "")
                        && it.value.meaningFilterValidated.approved != DictionaryMeaningApproved.NONE
                        && it.value.meaningFilterValidated.approved != ctx.meaningResponse.approved
            }.forEach {
                ctx.command = DictionaryCommand.DELETE
                it.key.sendResponse(appSettings, ctx, logId)
            }
    }
}
