package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningReadResponseTest : FunSpec ({

    val readResponseSuccess = MeaningReadResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "456",
            word = "трава",
            meaning = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t-o-n-y-p",
            approved = false
        )
    )

    val readResponseError = MeaningReadResponse(
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

    test("Serialize read response with success") {
        val json = apiV1Mapper.writeValueAsString(readResponseSuccess)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\"о чем-н. не имеющем вкуса, безвкусном \\(разг.\\)\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"t-o-n-y-p\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize read response with error") {
        val json = apiV1Mapper.writeValueAsString(readResponseError)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"code\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"exception\".*")
        json shouldMatch Regex(".*\"message\":\\s*\"error\".*")
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"proposedBy\":"
        json shouldNotMatch "\"approved\":"
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