package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningReadResponseTest : FunSpec ({

    val emptyReadResponse = MeaningReadResponse()
    val readResponseSuccess = MeaningReadResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "456",
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "unittest",
            approved = false
        ),
        errors = emptyList()
    )
    val readResponseError = MeaningReadResponse(
        requestId = "789",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseFullObject(),
        errors = listOf(
            Error(
                code = "123",
                message = "exception"
            ),
            Error(
                code = "456",
                message = "error"
            )
        )
    )

    test("Serialize empty read response") {
        val json = apiV1Mapper.writeValueAsString(emptyReadResponse)
        json shouldMatch Regex(".*\"responseType\":\\s*\"read\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"result\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
        json shouldNotMatch "\"approved\":"
        json shouldNotMatch "\"errors\":"
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize read response with success") {
        val json = apiV1Mapper.writeValueAsString(readResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"read\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"meaning\":\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"обвал\".*")
        json shouldMatch Regex(".*\"value\":\\s*\"снежные глыбы или обломки скал, обрушившиеся с гор\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"unittest\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*].*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize read response with error") {
        val json = apiV1Mapper.writeValueAsString(readResponseError)
        json shouldMatch Regex(".*\"responseType\":\\s*\"read\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"meaning\":\\{\\s*}.*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*[^]].*")
        json shouldMatch Regex(".*\"code\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Deserialize empty read response") {
        val json = apiV1Mapper.writeValueAsString(emptyReadResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningReadResponse

        val expectedReadResponseSuccess = emptyReadResponse.copy(responseType = "read")
        obj shouldBe expectedReadResponseSuccess
    }

    test("Deserialize read response with success") {
        val json = apiV1Mapper.writeValueAsString(readResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningReadResponse

        val expectedReadResponseSuccess = readResponseSuccess.copy(responseType = "read")
        obj shouldBe expectedReadResponseSuccess
    }

    test("Deserialize read response with error") {
        val json = apiV1Mapper.writeValueAsString(readResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningReadResponse

        val expectedReadResponseError = readResponseError.copy(responseType = "read")
        obj shouldBe expectedReadResponseError
    }

})