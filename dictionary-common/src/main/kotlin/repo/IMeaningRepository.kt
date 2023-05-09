package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryError

interface IMeaningRepository {

    suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse
    suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse
    suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse
    suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse
    suspend fun searchMeaning(rq: DbMeaningFilterRequest): DbMeaningsResponse
    companion object {
        val NONE = object : IMeaningRepository {
            override suspend fun createMeaning(rq: DbMeaningRequest) = TODO("Not yet implemented")

            override suspend fun readMeaning(rq: DbMeaningIdRequest) = TODO("Not yet implemented")

            override suspend fun updateMeaning(rq: DbMeaningRequest) = TODO("Not yet implemented")

            override suspend fun deleteMeaning(rq: DbMeaningIdRequest) = TODO("Not yet implemented")

            override suspend fun searchMeaning(rq: DbMeaningFilterRequest) = TODO("Not yet implemented")
        }
    }

    object Errors {

        val RESULT_ERROR_EMPTY_ID = DbMeaningResponse.error(
            DictionaryError(
                code = "ID_IS_EMPTY",
                message = "ID must not be empty"
            )
        )
        val RESULT_ERROR_EMPTY_VERSION = DbMeaningResponse.error(
            DictionaryError(
                code = "VERSION_IS_EMPTY",
                message = "Version must not be empty"
            )
        )
        val RESULT_ERROR_NOT_FOUND = DbMeaningResponse.error(
            DictionaryError(
                code = "NOT_FOUND",
                message = "Meaning with the provided ID doesn't exist"
            )
        )
        val RESULT_ERROR_ALREADY_EXISTS = DbMeaningResponse.error(
            DictionaryError(
                code = "ALREADY_EXISTS",
                message = "Meaning with the provided word and value already exists"
            )
        )
        val RESULT_ERROR_CONCURRENT_MODIFICATION = DbMeaningResponse.error(
            DictionaryError(
                code = "CONCURRENT_MODIFICATION",
                message = "The object has been changed concurrently by another user or process. " +
                        "Please reload the object and try again"
            )
        )

    }

}