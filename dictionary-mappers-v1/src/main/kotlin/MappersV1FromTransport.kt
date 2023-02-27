package com.tonyp.dictionarykotlin.mappers.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.mappers.v1.exceptions.UnknownRequestClass

fun DictionaryContext.fromTransport(request: IRequest) = when (request) {
    is MeaningCreateRequest -> fromTransport(request)
    is MeaningReadRequest -> fromTransport(request)
    is MeaningUpdateRequest -> fromTransport(request)
    is MeaningDeleteRequest -> fromTransport(request)
    is MeaningSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toMeaningId() = this?.let { DictionaryMeaningId(it) } ?: DictionaryMeaningId.NONE
private fun String?.toMeaningWithId() = DictionaryMeaning(id = this.toMeaningId())
private fun IRequest?.requestId() = this?.requestId?.let { DictionaryRequestId(it) } ?: DictionaryRequestId.NONE

private fun MeaningDebug?.transportToWorkMode(): DictionaryWorkMode = when (this?.mode) {
    MeaningRequestDebugMode.PROD -> DictionaryWorkMode.PROD
    MeaningRequestDebugMode.TEST -> DictionaryWorkMode.TEST
    MeaningRequestDebugMode.STUB -> DictionaryWorkMode.STUB
    null -> DictionaryWorkMode.PROD
}

private fun MeaningDebug?.transportToStubCase(): DictionaryStub = when (this?.stub) {
    MeaningRequestDebugStubs.SUCCESS -> DictionaryStub.SUCCESS
    MeaningRequestDebugStubs.CANNOT_CREATE -> DictionaryStub.CANNOT_CREATE
    MeaningRequestDebugStubs.CANNOT_READ -> DictionaryStub.CANNOT_READ
    MeaningRequestDebugStubs.CANNOT_UPDATE -> DictionaryStub.CANNOT_UPDATE
    MeaningRequestDebugStubs.CANNOT_DELETE -> DictionaryStub.CANNOT_DELETE
    MeaningRequestDebugStubs.CANNOT_SEARCH -> DictionaryStub.CANNOT_SEARCH
    null -> DictionaryStub.NONE
}

fun DictionaryContext.fromTransport(request: MeaningCreateRequest) {
    command = DictionaryCommand.CREATE
    requestId = request.requestId()
    meaningRequest = request.meaning?.toInternal() ?: DictionaryMeaning()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DictionaryContext.fromTransport(request: MeaningReadRequest) {
    command = DictionaryCommand.READ
    requestId = request.requestId()
    meaningRequest = request.meaning?.id.toMeaningWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DictionaryContext.fromTransport(request: MeaningUpdateRequest) {
    command = DictionaryCommand.UPDATE
    requestId = request.requestId()
    meaningRequest = request.meaning?.toInternal() ?: DictionaryMeaning()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DictionaryContext.fromTransport(request: MeaningDeleteRequest) {
    command = DictionaryCommand.DELETE
    requestId = request.requestId()
    meaningRequest = request.meaning?.id.toMeaningWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun DictionaryContext.fromTransport(request: MeaningSearchRequest) {
    command = DictionaryCommand.SEARCH
    requestId = request.requestId()
    meaningFilterRequest = request.meaningFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MeaningSearchFilter?.toInternal(): DictionaryMeaningFilter = DictionaryMeaningFilter(
    word = this?.word ?: "",
    approved = this?.approved.fromTransport()
)

private fun Boolean?.fromTransport(): DictionaryMeaningApproved = when (this) {
    false -> DictionaryMeaningApproved.FALSE
    true -> DictionaryMeaningApproved.TRUE
    null -> DictionaryMeaningApproved.NONE
}

private fun MeaningCreateObject.toInternal(): DictionaryMeaning = DictionaryMeaning(
    word = this.word ?: "",
    meaning = this.meaning ?: "",
    proposedBy = this.proposedBy ?: ""
)

private fun MeaningUpdateObject.toInternal(): DictionaryMeaning = DictionaryMeaning(
    id = this.id.toMeaningId(),
    approved = this.approved.fromTransport()
)