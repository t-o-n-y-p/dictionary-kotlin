package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.IRequest
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.asDictionaryError
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.log.v1.common.IDictionaryLogWrapper
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.base.toModel
import com.tonyp.dictionarykotlin.meaning.app.helpers.process
import com.tonyp.dictionarykotlin.meaning.app.helpers.sendResponse
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.datetime.Clock
import toLog

suspend inline fun <reified Q : IRequest> ApplicationCall.processCommand(
    command: DictionaryCommand? = null,
    appSettings: DictionaryAppSettings,
    logger: IDictionaryLogWrapper,
    logId: String
): DictionaryContext? {
    val context = DictionaryContext(
        timeStart = Clock.System.now(),
    )
    try {
        logger.doWithLogging(logId) {
            context.principal = principal<JWTPrincipal>().toModel()
            val request = receive<Q>()
            context.fromTransport(request)
            process(appSettings, context, logId)
            sendResponse(appSettings, context, logId)
        }
        return context
    } catch (e: Throwable) {
        logger.doWithLogging("$logId-failure") {
            command?.also { context.command = it }
            logger.error(
                msg = "$command handling failed",
                e = e,
                data = context.toLog(logger.loggerId)
            )
            context.fail(e.asDictionaryError())
            sendResponse(appSettings, context, logId)
        }
        return null
    }
}

suspend inline fun <reified Q : IRequest> WebSocketSession.processCommand(
    command: DictionaryCommand? = null,
    appSettings: DictionaryAppSettings,
    logger: IDictionaryLogWrapper,
    logId: String
) {
    incoming.receiveAsFlow().mapNotNull{
        val frame = it as? Frame.Text ?: return@mapNotNull
        val context = DictionaryContext(
            timeStart = Clock.System.now(),
        )
        sessions.put(this, context)
        logger.doWithLogging(logId) {
            val request = apiV1Mapper.readValue(frame.readText(), Q::class.java)
            context.fromTransport(request)
            process(appSettings, context, logId)
            sendResponse(appSettings, context, logId)
        }
    }.catch {
        if (it is ClosedReceiveChannelException) {
            sessions.clear()
        } else {
            val context = sessions[this@processCommand] ?: DictionaryContext()
            command?.also { c -> context.command = c }
            logger.doWithLogging("$logId-failure") {
                logger.error(
                    msg = "${DictionaryCommand.SEARCH} handling failed",
                    e = it,
                    data = context.toLog(logger.loggerId)
                )
                context.fail(it.asDictionaryError())
                sendResponse(appSettings, context, logId)
            }
        }
    }.collect()
}