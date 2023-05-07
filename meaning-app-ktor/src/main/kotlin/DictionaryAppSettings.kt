package com.tonyp.dictionarykotlin.meaning.app

import com.tonyp.dictionarykotlin.common.DictionaryCorSettings

data class DictionaryAppSettings (
    val appUrls: List<String>,
    val corSettings: DictionaryCorSettings
)