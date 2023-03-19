package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningSearchInitResponseTest : FunSpec ({

    val emptySearchInitResponse = MeaningSearchInitResponse()
    val searchInitResponseSuccess = MeaningSearchInitResponse(
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meanings = listOf(
            MeaningResponseFullObject(
                id = "456",
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "t-o-n-y-p",
                approved = true
            ),
            MeaningResponseFullObject(
                id = "123",
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "unittest",
                approved = false
            )
        ),
        webSocketExtensions = listOf(MeaningWebSocketExtension.DEFLATE),
        errors = emptyList()
    )
    val searchInitResponseError = MeaningSearchInitResponse(
        requestId = "123",
        result = ResponseResult.ERROR,
        meanings = emptyList(),
        errors = listOf(
            Error(
                code = "789",
                message = "exception"
            ),
            Error(
                code = "456",
                message = "error"
            )
        ),
        webSocketExtensions = emptyList()
    )

    test("Serialize empty search init response") {
        val json = apiV1Mapper.writeValueAsString(emptySearchInitResponse)
        json shouldMatch Regex(".*\"responseType\":\\s*\"search_init\".*")
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
        json shouldNotMatch "\"webSocketExtensions\":"
    }

    test("Serialize search init response with success") {
        val json = apiV1Mapper.writeValueAsString(searchInitResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"search_init\".*")
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
        json shouldMatch Regex(".*\"webSocketExtensions\":\\s*\\[\\s*\"deflate\"\\s*].*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize search init response with error") {
        val json = apiV1Mapper.writeValueAsString(searchInitResponseError)
        json shouldMatch Regex(".*\"responseType\":\\s*\"search_init\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"meanings\":\\s*\\[\\s*].*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*[^]].*")
        json shouldMatch Regex(".*\"code\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"webSocketExtensions\":\\s*\\[\\s*].*")
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Deserialize empty search init response") {
        val json = apiV1Mapper.writeValueAsString(emptySearchInitResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningSearchInitResponse

        val expectedSearchResponseSuccess = emptySearchInitResponse.copy(responseType = "search_init")
        obj shouldBe expectedSearchResponseSuccess
    }

    test("Deserialize search init response with success") {
        val json = apiV1Mapper.writeValueAsString(searchInitResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningSearchInitResponse

        val expectedSearchResponseSuccess = searchInitResponseSuccess.copy(responseType = "search_init")
        obj shouldBe expectedSearchResponseSuccess
    }

    test("Deserialize search init response with error") {
        val json = apiV1Mapper.writeValueAsString(searchInitResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningSearchInitResponse

        val expectedSearchResponseError = searchInitResponseError.copy(responseType = "search_init")
        obj shouldBe expectedSearchResponseError
    }

})