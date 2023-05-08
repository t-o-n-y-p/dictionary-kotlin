package com.tonyp.dictionarykotlin.repo.stub

import com.tonyp.dictionarykotlin.common.repo.*
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub

class MeaningRepoStub : IMeaningRepository {

    override suspend fun createMeaning(rq: DbMeaningRequest) =
        DbMeaningResponse.success(DictionaryMeaningStub.getPending())

    override suspend fun readMeaning(rq: DbMeaningIdRequest) =
        DbMeaningResponse.success(DictionaryMeaningStub.getApproved())

    override suspend fun updateMeaning(rq: DbMeaningRequest) =
        DbMeaningResponse.success(DictionaryMeaningStub.getApproved())

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest) =
        DbMeaningResponse.success(DictionaryMeaningStub.getPending())

    override suspend fun searchMeaning(rq: DbMeaningFilterRequest) =
        DbMeaningsResponse.success(DictionaryMeaningStub.getSearchResult())

}