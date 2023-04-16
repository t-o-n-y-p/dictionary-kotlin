package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.helpers.process
import com.tonyp.dictionarykotlin.meaning.app.helpers.sendResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createMeaning(appSettings: DictionaryAppSettings) {
    val logId = "create"
    val logger = appSettings.corSettings.loggerProvider.logger(ApplicationCall::createMeaning)
    logger.doWithLogging(logId) {
        val request = receive<MeaningCreateRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        process(appSettings, context)
        sendResponse(appSettings, context)
        sendWebSocketCreateDeleteNotification(appSettings, context)
    }
}

suspend fun ApplicationCall.readMeaning(appSettings: DictionaryAppSettings) {
    val logId = "read"
    val logger = appSettings.corSettings.loggerProvider.logger(ApplicationCall::readMeaning)
    logger.doWithLogging(logId) {
        val request = receive<MeaningReadRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        process(appSettings, context)
        sendResponse(appSettings, context)
    }
}

suspend fun ApplicationCall.updateMeaning(appSettings: DictionaryAppSettings) {
    val logId = "update"
    val logger = appSettings.corSettings.loggerProvider.logger(ApplicationCall::updateMeaning)
    logger.doWithLogging(logId) {
        val request = receive<MeaningUpdateRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        process(appSettings, context)
        sendResponse(appSettings, context)
        sendWebSocketUpdateNotification(appSettings, context)
    }
}

suspend fun ApplicationCall.deleteMeaning(appSettings: DictionaryAppSettings) {
    val logId = "delete"
    val logger = appSettings.corSettings.loggerProvider.logger(ApplicationCall::deleteMeaning)
    logger.doWithLogging(logId) {
        val request = receive<MeaningDeleteRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        process(appSettings, context)
        sendResponse(appSettings, context)
        sendWebSocketCreateDeleteNotification(appSettings, context)
    }
}

suspend fun ApplicationCall.searchMeaning(appSettings: DictionaryAppSettings) {
    val logId = "search"
    val logger = appSettings.corSettings.loggerProvider.logger(ApplicationCall::searchMeaning)
    logger.doWithLogging(logId) {
        val request = receive<MeaningSearchRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        process(appSettings, context)
        sendResponse(appSettings, context)
    }
}
