package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<DictionaryError>
}