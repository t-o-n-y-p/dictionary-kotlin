package com.tonyp.dictionarykotlin.meaning.app

import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.DictionaryCorSettings

data class DictionaryAppSettings (
    val appUrls: List<String> = emptyList(),
    val corSettings: DictionaryCorSettings,
    val processor: DictionaryMeaningProcessor = DictionaryMeaningProcessor(settings = corSettings)
)