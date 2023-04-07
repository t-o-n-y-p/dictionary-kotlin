package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import toLog

suspend fun ApplicationCall.createMeaning(appSettings: DictionaryAppSettings) {
    val logId = "create"
    val logger = appSettings.corSettings.loggerProvider
        .logger(ApplicationCall::createMeaning::class.qualifiedName ?: logId)
    logger.doWithLogging(logId) {
        val request = receive<MeaningCreateRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is received",
            data = context.toLog("${logId}-request")
        )
        context.meaningResponse = DictionaryMeaningStub.getPending()
        respond(context.toTransportMeaning())
        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
        sendWebSocketCreateDeleteNotification(context)
    }
}

suspend fun ApplicationCall.readMeaning(appSettings: DictionaryAppSettings) {
    val logId = "read"
    val logger = appSettings.corSettings.loggerProvider
        .logger(ApplicationCall::readMeaning::class.qualifiedName ?: logId)
    logger.doWithLogging(logId) {
        val request = receive<MeaningReadRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is received",
            data = context.toLog("${logId}-request")
        )
        context.meaningResponse = DictionaryMeaningStub.getApproved()
        respond(context.toTransportMeaning())
        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}

suspend fun ApplicationCall.updateMeaning(appSettings: DictionaryAppSettings) {
    val logId = "update"
    val logger = appSettings.corSettings.loggerProvider
        .logger(ApplicationCall::updateMeaning::class.qualifiedName ?: logId)
    logger.doWithLogging(logId) {
        val request = receive<MeaningUpdateRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is received",
            data = context.toLog("${logId}-request")
        )
        context.meaningResponse = DictionaryMeaningStub.getApproved()
        respond(context.toTransportMeaning())
        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
        sendWebSocketUpdateNotification(context)
    }
}

suspend fun ApplicationCall.deleteMeaning(appSettings: DictionaryAppSettings) {
    val logId = "delete"
    val logger = appSettings.corSettings.loggerProvider
        .logger(ApplicationCall::deleteMeaning::class.qualifiedName ?: logId)
    logger.doWithLogging(logId) {
        val request = receive<MeaningDeleteRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is received",
            data = context.toLog("${logId}-request")
        )
        context.meaningResponse = DictionaryMeaningStub.getPending()
        respond(context.toTransportMeaning())
        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
        sendWebSocketCreateDeleteNotification(context)
    }
}

suspend fun ApplicationCall.searchMeaning(appSettings: DictionaryAppSettings) {
    val logId = "search"
    val logger = appSettings.corSettings.loggerProvider
        .logger(ApplicationCall::searchMeaning::class.qualifiedName ?: logId)
    logger.doWithLogging(logId) {
        val request = receive<MeaningSearchRequest>()
        val context = DictionaryContext()
        context.fromTransport(request)
        logger.info(
            msg = "${context.command} request is received",
            data = context.toLog("${logId}-request")
        )
        context.meaningsResponse.addAll(DictionaryMeaningStub.getSearchResult())
        respond(context.toTransportMeaning())
        logger.info(
            msg = "${context.command} response is sent",
            data = context.toLog("${logId}-response")
        )
    }
}
