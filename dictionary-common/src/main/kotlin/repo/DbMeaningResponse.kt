package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning

data class DbMeaningResponse (
    override val data: DictionaryMeaning?,
    override val isSuccess: Boolean,
    override val errors: List<DictionaryError> = emptyList()
) : IDbResponse<DictionaryMeaning> {
    companion object {
        val MOCK_SUCCESS_EMPTY = DbMeaningResponse(null, true)
        fun success(result: DictionaryMeaning) = DbMeaningResponse(result, true)
        fun error(errors: List<DictionaryError>) = DbMeaningResponse(null, false, errors)
        fun error(error: DictionaryError) = DbMeaningResponse(null, false, listOf(error))
    }
}
