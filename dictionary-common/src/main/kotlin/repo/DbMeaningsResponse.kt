package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning

data class DbMeaningsResponse (
    override val data: List<DictionaryMeaning>?,
    override val isSuccess: Boolean,
    override val errors: List<DictionaryError> = emptyList()
) : IDbResponse<List<DictionaryMeaning>> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbMeaningsResponse(emptyList(), true)
        fun success(result: List<DictionaryMeaning>) = DbMeaningsResponse(result, true)
        fun error(errors: List<DictionaryError>) = DbMeaningsResponse(null, false, errors)
        fun error(error: DictionaryError) = DbMeaningsResponse(null, false, listOf(error))
    }
}
