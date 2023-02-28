package com.tonyp.dictionarykotlin.mappers.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.exceptions.UnknownDictionaryCommand

fun DictionaryContext.toTransport(): IResponse = when (val cmd = command) {
    DictionaryCommand.CREATE -> toTransportCreate()
    DictionaryCommand.READ -> toTransportRead()
    DictionaryCommand.UPDATE -> toTransportUpdate()
    DictionaryCommand.DELETE -> toTransportDelete()
    DictionaryCommand.SEARCH -> toTransportSearch()
    DictionaryCommand.NONE -> throw UnknownDictionaryCommand(cmd)
}

fun DictionaryContext.toTransportCreate() = MeaningCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportFullMeaning()
)

fun DictionaryContext.toTransportRead() = MeaningReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportFullMeaning()
)

fun DictionaryContext.toTransportUpdate() = MeaningUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportFullMeaning()
)

fun DictionaryContext.toTransportDelete() = MeaningDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportDeleteMeaning()
)

fun DictionaryContext.toTransportSearch() = MeaningSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meanings = meaningsResponse.toTransportFullMeanings()
)

fun List<DictionaryMeaning>.toTransportFullMeanings(): List<MeaningResponseFullObject>? = this
    .map { it.toTransportFullMeaning() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun DictionaryMeaning.toTransportFullMeaning(): MeaningResponseFullObject = MeaningResponseFullObject(
    id = id.takeIf { it != DictionaryMeaningId.NONE }?.asString(),
    word = word.takeIf { it.isNotBlank() },
    value = value.takeIf { it.isNotBlank() },
    proposedBy = proposedBy.takeIf { it.isNotBlank() },
    approved = approved.toTransportMeaning()
)

private fun DictionaryMeaning.toTransportDeleteMeaning(): MeaningResponseDeleteObject = MeaningResponseDeleteObject(
    id = id.takeIf { it != DictionaryMeaningId.NONE }?.asString()
)

private fun DictionaryMeaningApproved.toTransportMeaning() : Boolean? = when (this) {
    DictionaryMeaningApproved.FALSE -> false
    DictionaryMeaningApproved.TRUE -> true
    DictionaryMeaningApproved.NONE -> null
}

private fun List<DictionaryError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportMeaning() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun DictionaryError.toTransportMeaning() = Error(
    code = code.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)