package com.tonyp.dictionarykotlin.common.models

data class DictionaryError (

    val code: String = "",
    val message: String = "",
    val exception: Throwable? = null,
    val level: Level = Level.ERROR,
) {
    enum class Level {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
