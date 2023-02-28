package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningSearchResponseTest : FunSpec ({

    val emptySearchResponse = MeaningSearchResponse()
    val searchResponseSuccess = MeaningSearchResponse(
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meanings = listOf(
            MeaningResponseFullObject(
                id = "123",
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "t-o-n-y-p",
                approved = false
            ),
            MeaningResponseFullObject(
                id = "456",
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "unittest",
                approved = true
            )
        ),
        errors = emptyList()
    )
    val searchResponseError = MeaningSearchResponse(
        requestId = "456",
        result = ResponseResult.ERROR,
        meanings = emptyList(),
        errors = listOf(
            Error(
                code = "789",
                message = "exception"
            ),
            Error(
                code = "123",
                message = "error"
            )
        )
    )

    test("Serialize empty search response") {
        val json = apiV1Mapper.writeValueAsString(emptySearchResponse)
        json shouldMatch Regex(".*\"responseType\":\\s*\"search\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"result\":"
        json shouldNotMatch "\"meanings\":"
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
        json shouldNotMatch "\"approved\":"
        json shouldNotMatch "\"errors\":"
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize search response with success") {
        val json = apiV1Mapper.writeValueAsString(searchResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"search\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"meanings\":\\s*\\[\\s*[^]].*")
        json shouldMatch Regex(".*\"id\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"обвал\".*")
        json shouldMatch Regex(".*\"value\":\\s*\"о чем-н. не имеющем вкуса, безвкусном \\(разг.\\)\".*")
        json shouldMatch Regex(".*\"value\":\\s*\"снежные глыбы или обломки скал, обрушившиеся с гор\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"t-o-n-y-p\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"unittest\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
        json shouldMatch Regex(".*\"approved\":\\s*true.*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*].*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize search response with error") {
        val json = apiV1Mapper.writeValueAsString(searchResponseError)
        json shouldMatch Regex(".*\"responseType\":\\s*\"search\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"meanings\":\\s*\\[\\s*].*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*[^]].*")
        json shouldMatch Regex(".*\"code\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Deserialize empty search response") {
        val json = apiV1Mapper.writeValueAsString(emptySearchResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningSearchResponse

        val expectedSearchResponseSuccess = emptySearchResponse.copy(responseType = "search")
        obj shouldBe expectedSearchResponseSuccess
    }

    test("Deserialize search response with success") {
        val json = apiV1Mapper.writeValueAsString(searchResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningSearchResponse

        val expectedSearchResponseSuccess = searchResponseSuccess.copy(responseType = "search")
        obj shouldBe expectedSearchResponseSuccess
    }

    test("Deserialize search response with error") {
        val json = apiV1Mapper.writeValueAsString(searchResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningSearchResponse

        val expectedSearchResponseError = searchResponseError.copy(responseType = "search")
        obj shouldBe expectedSearchResponseError
    }

})