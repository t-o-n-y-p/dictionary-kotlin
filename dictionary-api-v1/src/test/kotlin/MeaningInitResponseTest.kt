package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningInitResponseTest : FunSpec ({

    val emptyInitResponse = MeaningInitResponse()
    val initResponseSuccess = MeaningInitResponse(
        requestId = "789",
        result = ResponseResult.SUCCESS,
        webSocketExtensions = listOf(MeaningWebSocketExtension.DEFLATE),
        errors = emptyList()
    )
    val initResponseError = MeaningInitResponse(
        requestId = "123",
        result = ResponseResult.ERROR,
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

    test("Serialize empty init response") {
        val json = apiV1Mapper.writeValueAsString(emptyInitResponse)
        json shouldMatch Regex(".*\"responseType\":\\s*\"init\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"result\":"
        json shouldNotMatch "\"errors\":"
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
        json shouldNotMatch "\"webSocketExtensions\":"
    }

    test("Serialize init response with success") {
        val json = apiV1Mapper.writeValueAsString(initResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"init\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*].*")
        json shouldMatch Regex(".*\"webSocketExtensions\":\\s*\\[\\s*\"deflate\"\\s*].*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize init response with error") {
        val json = apiV1Mapper.writeValueAsString(initResponseError)
        json shouldMatch Regex(".*\"responseType\":\\s*\"init\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*[^]].*")
        json shouldMatch Regex(".*\"code\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"webSocketExtensions\":\\s*\\[\\s*].*")
    }

    test("Deserialize empty init response") {
        val json = apiV1Mapper.writeValueAsString(emptyInitResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningInitResponse

        val expectedSearchResponseSuccess = emptyInitResponse.copy(responseType = "init")
        obj shouldBe expectedSearchResponseSuccess
    }

    test("Deserialize init response with success") {
        val json = apiV1Mapper.writeValueAsString(initResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningInitResponse

        val expectedSearchResponseSuccess = initResponseSuccess.copy(responseType = "init")
        obj shouldBe expectedSearchResponseSuccess
    }

    test("Deserialize init response with error") {
        val json = apiV1Mapper.writeValueAsString(initResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningInitResponse

        val expectedSearchResponseError = initResponseError.copy(responseType = "init")
        obj shouldBe expectedSearchResponseError
    }

})