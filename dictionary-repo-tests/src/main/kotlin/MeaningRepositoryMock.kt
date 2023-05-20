package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.repo.*

class MeaningRepositoryMock(
    private val invokeCreate: (DbMeaningRequest) -> DbMeaningResponse = { DbMeaningResponse.MOCK_SUCCESS_EMPTY },
    private val invokeRead: (DbMeaningIdRequest) -> DbMeaningResponse = { DbMeaningResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdate: (DbMeaningRequest) -> DbMeaningResponse = { DbMeaningResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDelete: (DbMeaningIdRequest) -> DbMeaningResponse = { DbMeaningResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearch: (DbMeaningFilterRequest) -> DbMeaningsResponse = { DbMeaningsResponse.MOCK_SUCCESS_EMPTY },
) : IMeaningRepository {

    override suspend fun createMeaning(rq: DbMeaningRequest) = invokeCreate(rq)

    override suspend fun readMeaning(rq: DbMeaningIdRequest) = invokeRead(rq)

    override suspend fun updateMeaning(rq: DbMeaningRequest) = invokeUpdate(rq)

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest) = invokeDelete(rq)

    override suspend fun searchMeaning(rq: DbMeaningFilterRequest) = invokeSearch(rq)
}