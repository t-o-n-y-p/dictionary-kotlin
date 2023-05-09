package com.tonyp.dictionarykotlin.common

import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub

import kotlinx.datetime.Instant

data class DictionaryContext (

    var command: DictionaryCommand = DictionaryCommand.NONE,
    var state: DictionaryState = DictionaryState.NONE,
    val errors: MutableList<DictionaryError> = mutableListOf(),
    var settings: DictionaryCorSettings = DictionaryCorSettings.NONE,

    var workMode: DictionaryWorkMode = DictionaryWorkMode.PROD,
    var stubCase: DictionaryStub = DictionaryStub.NONE,

    var meaningRepo: IMeaningRepository = IMeaningRepository.NONE,
    var meaningRepoRead: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningRepoPrepare: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningRepoDone: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningsRepoDone: MutableList<DictionaryMeaning> = mutableListOf(),

    var webSocketExtensions: MutableList<DictionaryWebSocketExtension> = mutableListOf(),

    var requestId: DictionaryRequestId = DictionaryRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var meaningRequest: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningFilterRequest: DictionaryMeaningFilter = DictionaryMeaningFilter(),

    var meaningValidating: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningFilterValidating: DictionaryMeaningFilter = DictionaryMeaningFilter(),

    var meaningValidated: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningFilterValidated: DictionaryMeaningFilter = DictionaryMeaningFilter(),

    var meaningResponse: DictionaryMeaning = DictionaryMeaning.NONE,
    var meaningsResponse: MutableList<DictionaryMeaning> = mutableListOf()

)