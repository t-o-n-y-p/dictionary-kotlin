package com.tonyp.dictionarykotlin.mappers.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.exceptions.UnknownDictionaryCommand

fun DictionaryContext.toTransportMeaning(): IResponse = when (val cmd = command) {
    DictionaryCommand.CREATE -> toTransportCreate()
    DictionaryCommand.READ -> toTransportRead()
    DictionaryCommand.UPDATE -> toTransportUpdate()
    DictionaryCommand.DELETE -> toTransportDelete()
    DictionaryCommand.SEARCH -> toTransportSearch()
    DictionaryCommand.SEARCH_INIT -> toTransportSearchInit()
    DictionaryCommand.NONE -> throw UnknownDictionaryCommand(cmd)
}

private fun DictionaryContext.toTransportCreate() = MeaningCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportFullMeaning()
)

private fun DictionaryContext.toTransportRead() = MeaningReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportFullMeaning()
)

private fun DictionaryContext.toTransportUpdate() = MeaningUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportFullMeaning()
)

private fun DictionaryContext.toTransportDelete() = MeaningDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meaning = meaningResponse.toTransportDeleteMeaning()
)

private fun DictionaryContext.toTransportSearch() = MeaningSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    meanings = meaningsResponse.toTransportFullMeanings()
)

private fun DictionaryContext.toTransportSearchInit() = MeaningSearchInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == DictionaryState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    webSocketExtensions = webSocketExtensions.toTransportExtensions(),
    meanings = meaningsResponse.toTransportFullMeanings()
)

private fun List<DictionaryMeaning>.toTransportFullMeanings(): List<MeaningResponseFullObject>? = this
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

private fun List<DictionaryWebSocketExtension>.toTransportExtensions(): List<MeaningWebSocketExtension> = this
    .mapNotNull { it.toTransportExtension() }
    .toList()

private fun DictionaryError.toTransportMeaning() = Error(
    code = code.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)

private fun DictionaryWebSocketExtension.toTransportExtension() = when (this) {
    DictionaryWebSocketExtension.DEFLATE -> MeaningWebSocketExtension.DEFLATE
    DictionaryWebSocketExtension.NONE -> null
}