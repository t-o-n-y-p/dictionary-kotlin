package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createMeaning() {
    val request = receive<MeaningCreateRequest>()
    val context = DictionaryContext()
    context.fromTransport(request)
    context.meaningResponse = DictionaryMeaningStub.getPending()
    respond(context.toTransportMeaning())
}

suspend fun ApplicationCall.readMeaning() {
    val request = receive<MeaningReadRequest>()
    val context = DictionaryContext()
    context.fromTransport(request)
    context.meaningResponse = DictionaryMeaningStub.getApproved()
    respond(context.toTransportMeaning())
}

suspend fun ApplicationCall.updateMeaning() {
    val request = receive<MeaningUpdateRequest>()
    val context = DictionaryContext()
    context.fromTransport(request)
    context.meaningResponse = DictionaryMeaningStub.getApproved()
    respond(context.toTransportMeaning())
}

suspend fun ApplicationCall.deleteMeaning() {
    val request = receive<MeaningDeleteRequest>()
    val context = DictionaryContext()
    context.fromTransport(request)
    context.meaningResponse = DictionaryMeaningStub.getPending()
    respond(context.toTransportMeaning())
}

suspend fun ApplicationCall.searchMeaning() {
    val request = receive<MeaningSearchRequest>()
    val context = DictionaryContext()
    context.fromTransport(request)
    context.meaningsResponse.addAll(DictionaryMeaningStub.getSearchResult())
    respond(context.toTransportMeaning())
}
