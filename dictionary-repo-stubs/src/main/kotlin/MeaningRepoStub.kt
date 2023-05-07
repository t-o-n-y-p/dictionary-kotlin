package com.tonyp.dictionarykotlin.repo.stub

import com.tonyp.dictionarykotlin.common.repo.*
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub

class MeaningRepoStub : IMeaningRepository {

    override suspend fun createMeaning(rq: DbMeaningRequest) = DbMeaningResponse(
        data = DictionaryMeaningStub.getPending(),
        isSuccess = true
    )

    override suspend fun readMeaning(rq: DbMeaningIdRequest) = DbMeaningResponse(
        data = DictionaryMeaningStub.getApproved(),
        isSuccess = true
    )

    override suspend fun updateMeaning(rq: DbMeaningRequest) = DbMeaningResponse(
        data = DictionaryMeaningStub.getApproved(),
        isSuccess = true
    )

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest) = DbMeaningResponse(
        data = DictionaryMeaningStub.getPending(),
        isSuccess = true
    )

    override suspend fun searchMeaning(rq: DbMeaningFilterRequest) = DbMeaningsResponse(
        data = DictionaryMeaningStub.getSearchResult(),
        isSuccess = true
    )
}