package com.tonyp.dictionarykotlin.common.models

data class DictionaryError (

    val code: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)
