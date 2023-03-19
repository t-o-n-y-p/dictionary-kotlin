package com.tonyp.dictionarykotlin.common.helpers

import com.tonyp.dictionarykotlin.common.models.DictionaryError

fun Exception.asDictionaryError(code: String = "unknown") =
    DictionaryError(
        code = code,
        message = this.message ?: "",
        exception = this
    )
