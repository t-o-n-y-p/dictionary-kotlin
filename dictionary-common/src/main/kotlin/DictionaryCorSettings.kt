package com.tonyp.dictionarykotlin.common

import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.log.v1.common.DictionaryLoggerProvider

data class DictionaryCorSettings(
    val loggerProvider: DictionaryLoggerProvider = DictionaryLoggerProvider(),
    val repositories: Map<DictionaryWorkMode, IMeaningRepository> =
        DictionaryWorkMode.values().associateWith { IMeaningRepository.NONE }
) {
    companion object {
        val NONE = DictionaryCorSettings()
    }
}