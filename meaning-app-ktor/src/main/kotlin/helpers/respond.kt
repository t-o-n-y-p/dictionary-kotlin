package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.v1.sessions
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import toLog

suspend fun ApplicationCall.sendResponse(appSettings: DictionaryAppSettings, ctx: DictionaryContext, logId: String) {
    val logger = appSettings.corSettings.loggerProvider.logger(::sendResponse)
    logger.doWithLogging {
        respond(ctx.toTransportMeaning())
        logger.info(
            msg = "$logId response is sent",
            data = ctx.toLog("$logId-response")
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun WebSocketSession.sendResponse(appSettings: DictionaryAppSettings, ctx: DictionaryContext, logId: String) {
    val logger = appSettings.corSettings.loggerProvider.logger(::sendResponse)
    try {
        logger.doWithLogging {
            send(apiV1Mapper.writeValueAsString(ctx.toTransportMeaning()))
            logger.info(
                msg = "$logId response is sent",
                data = ctx.toLog("$logId-response")
            )
        }
    } catch (_: Exception) {
        if (outgoing.isClosedForSend) {
            sessions.remove(this)
        }
    }
}