package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningDeleteResponseTest : FunSpec ({

    val deleteResponseSuccess = MeaningDeleteResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseDeleteObject(
            id = "456"
        )
    )

    val deleteResponseError = MeaningDeleteResponse(
        requestId = "456",
        result = ResponseResult.ERROR,
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

    test("Serialize delete response with success") {
        val json = apiV1Mapper.writeValueAsString(deleteResponseSuccess)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize delete response with error") {
        val json = apiV1Mapper.writeValueAsString(deleteResponseError)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldNotMatch "\"id\":"
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