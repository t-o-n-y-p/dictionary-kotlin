package com.tonyp.dictionarykotlin.common.helpers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryState

fun Throwable.asDictionaryError(code: String = "unknown") =
    DictionaryError(
        code = code,
        message = this.message ?: "",
        exception = this
    )

fun DictionaryContext.fail(vararg errors: DictionaryError) {
    this.errors.addAll(errors)
    state = DictionaryState.FAILING
}