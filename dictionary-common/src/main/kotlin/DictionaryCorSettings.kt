package com.tonyp.dictionarykotlin.common

import com.tonyp.dictionarykotlin.log.v1.common.DictionaryLoggerProvider

data class DictionaryCorSettings(
    val loggerProvider: DictionaryLoggerProvider = DictionaryLoggerProvider(),
) {
    companion object {
        val NONE = DictionaryCorSettings()
    }
}