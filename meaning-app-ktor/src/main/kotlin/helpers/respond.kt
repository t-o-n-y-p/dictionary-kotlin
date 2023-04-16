package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.websocket.*
import toLog

suspend fun ApplicationCall.sendResponse(appSettings: DictionaryAppSettings, ctx: DictionaryContext) {
    val logger = appSettings.corSettings.loggerProvider.logger(::sendResponse)
    logger.doWithLogging {
        respond(ctx.toTransportMeaning())
        logger.info(
            msg = "${ctx.command} response is sent",
            data = ctx.toLog("${ctx.command.name.lowercase()}-response")
        )
    }
}

suspend fun WebSocketSession.sendResponse(appSettings: DictionaryAppSettings, ctx: DictionaryContext) {
    val logger = appSettings.corSettings.loggerProvider.logger(::sendResponse)
    logger.doWithLogging {
        send(apiV1Mapper.writeValueAsString(ctx.toTransportMeaning()))
        logger.info(
            msg = "${ctx.command} response is sent",
            data = ctx.toLog("${ctx.command.name.lowercase()}-response")
        )
    }
}