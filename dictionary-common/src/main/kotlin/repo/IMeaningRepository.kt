package com.tonyp.dictionarykotlin.common.repo

interface IMeaningRepository {

    suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse
    suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse
    suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse
    suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse
    suspend fun searchMeaning(rq: DbMeaningFilterRequest): DbMeaningsResponse
    companion object {
        val NONE = object : IMeaningRepository {
            override suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse {
                TODO("Not yet implemented")
            }

            override suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse {
                TODO("Not yet implemented")
            }

            override suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse {
                TODO("Not yet implemented")
            }

            override suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse {
                TODO("Not yet implemented")
            }

            override suspend fun searchMeaning(rq: DbMeaningFilterRequest): DbMeaningsResponse {
                TODO("Not yet implemented")
            }
        }
    }

}