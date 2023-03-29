package com.tonyp.dictionarykotlin.common

import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub

import kotlinx.datetime.Instant

data class DictionaryContext (

    var command: DictionaryCommand = DictionaryCommand.NONE,
    var state: DictionaryState = DictionaryState.NONE,
    val errors: MutableList<DictionaryError> = mutableListOf(),

    var workMode: DictionaryWorkMode = DictionaryWorkMode.PROD,
    var stubCase: DictionaryStub = DictionaryStub.NONE,

    var webSocketExtensions: MutableList<DictionaryWebSocketExtension> = mutableListOf(),

    var requestId: DictionaryRequestId = DictionaryRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var meaningRequest: DictionaryMeaning = DictionaryMeaning(),
    var meaningFilterRequest: DictionaryMeaningFilter = DictionaryMeaningFilter(),
    var meaningResponse: DictionaryMeaning = DictionaryMeaning(),
    var meaningsResponse: MutableList<DictionaryMeaning> = mutableListOf()

)