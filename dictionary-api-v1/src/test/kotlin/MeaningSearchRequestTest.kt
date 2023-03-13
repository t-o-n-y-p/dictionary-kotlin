package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningSearchRequestTest : FunSpec ({

    val emptySearchRequest = MeaningSearchRequest()
    val firstSearchRequest = MeaningSearchRequest(
        debug = MeaningDebug(),
        meaningFilter = MeaningSearchFilter()
    )
    val secondSearchRequest = MeaningSearchRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_SEARCH
        ),
        meaningFilter = MeaningSearchFilter(
            word = "трава",
            approved = false
        )
    )

    test("Serialize empty search request") {
        val json = apiV1Mapper.writeValueAsString(emptySearchRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"search\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"debug\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Serialize search request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstSearchRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"search\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*}.*")
        json shouldMatch Regex(".*\"meaningFilter\":\\s*\\{\\s*}.*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Serialize search request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondSearchRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"search\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotSearch\".*")
        json shouldMatch Regex(".*\"meaningFilter\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
    }

    test("Deserialize empty search request") {
        val json = apiV1Mapper.writeValueAsString(emptySearchRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningSearchRequest

        val expectedFirstSearchRequest = emptySearchRequest.copy(requestType = "search")
        obj shouldBe expectedFirstSearchRequest
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