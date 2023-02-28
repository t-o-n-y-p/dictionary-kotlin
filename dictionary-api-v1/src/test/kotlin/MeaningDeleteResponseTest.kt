package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningDeleteResponseTest : FunSpec ({

    val emptyDeleteResponse = MeaningDeleteResponse()
    val deleteResponseSuccess = MeaningDeleteResponse(
        requestId = "456",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseDeleteObject(
            id = "789"
        ),
        errors = emptyList()
    )
    val deleteResponseError = MeaningDeleteResponse(
        requestId = "123",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseDeleteObject(),
        errors = listOf(
            Error(
                code = "456",
                message = "exception"
            ),
            Error(
                code = "789",
                message = "error"
            )
        )
    )

    test("Serialize empty delete response") {
        val json = apiV1Mapper.writeValueAsString(emptyDeleteResponse)
        json shouldMatch Regex(".*\"responseType\":\\s*\"delete\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"result\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"errors\":"
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize delete response with success") {
        val json = apiV1Mapper.writeValueAsString(deleteResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"delete\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"meaning\":\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*].*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize delete response with error") {
        val json = apiV1Mapper.writeValueAsString(deleteResponseError)
        json shouldMatch Regex(".*\"responseType\":\\s*\"delete\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"meaning\":\\{\\s*}.*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*[^]].*")
        json shouldMatch Regex(".*\"code\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldNotMatch "\"id\":"
    }

    test("Deserialize empty delete response") {
        val json = apiV1Mapper.writeValueAsString(emptyDeleteResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningDeleteResponse

        val expectedDeleteResponseSuccess = emptyDeleteResponse.copy(responseType = "delete")
        obj shouldBe expectedDeleteResponseSuccess
    }

    test("Deserialize delete response with success") {
        val json = apiV1Mapper.writeValueAsString(deleteResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningDeleteResponse

        val expectedDeleteResponseSuccess = deleteResponseSuccess.copy(responseType = "delete")
        obj shouldBe expectedDeleteResponseSuccess
    }

    test("Deserialize delete response with error") {
        val json = apiV1Mapper.writeValueAsString(deleteResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningDeleteResponse

        val expectedDeleteResponseError = deleteResponseError.copy(responseType = "delete")
        obj shouldBe expectedDeleteResponseError
    }

})