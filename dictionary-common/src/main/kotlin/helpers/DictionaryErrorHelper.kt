package com.tonyp.dictionarykotlin.common.helpers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryState

fun Throwable.asDictionaryError(
    code: String = "UNKNOWN_ERROR",
    message: String = "Something went wrong, please try again later"
) =
    DictionaryError(
        code = code,
        message = message,
        exception = this
    )

fun DictionaryContext.fail(vararg errors: DictionaryError) {
    fail(errors.asList())
}

fun DictionaryContext.fail(errors: List<DictionaryError>) {
    this.errors.addAll(errors)
    state = DictionaryState.FAILING
}

fun errorValidation(
    code: String,
    message: String,
    level: DictionaryError.Level = DictionaryError.Level.ERROR
) = DictionaryError(
    code = code,
    message = message,
    level = level
)

fun errorAdministration(
    code: String,
    message: String,
    level: DictionaryError.Level = DictionaryError.Level.ERROR,
    exception: Exception? = null
) = DictionaryError(
    code = code,
    message = message,
    level = level,
    exception = exception
)