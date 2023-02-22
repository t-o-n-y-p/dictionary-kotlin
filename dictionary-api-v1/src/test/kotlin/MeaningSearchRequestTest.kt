package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningSearchRequestTest : FunSpec ({

    val firstSearchRequest = MeaningSearchRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.TEST
        ),
        meaningFilter = MeaningSearchFilter(
            word = "трава"
        )
    )
    val secondSearchRequest = MeaningSearchRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_SEARCH
        ),
        meaningFilter = MeaningSearchFilter(
            word = "обвал",
            approved = false
        )
    )

    test("Serialize search request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstSearchRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"test\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Serialize search request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondSearchRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotSearch\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"обвал\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
    }

    test("Deserialize search request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstSearchRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningSearchRequest

        val expectedFirstSearchRequest = firstSearchRequest.copy(requestType = "search")
        obj shouldBe expectedFirstSearchRequest
    }

    test("Deserialize search request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondSearchRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningSearchRequest

        val expectedSecondSearchRequest = secondSearchRequest.copy(requestType = "search")
        obj shouldBe expectedSecondSearchRequest
    }

})