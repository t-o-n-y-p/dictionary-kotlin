package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningCreateResponseTest : FunSpec ({

    val emptyCreateResponse = MeaningCreateResponse()
    val createResponseSuccess = MeaningCreateResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "456",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t-o-n-y-p",
            approved = false
        ),
        errors = emptyList()
    )
    val createResponseError = MeaningCreateResponse(
        requestId = "456",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseFullObject(),
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

    test("Serialize empty create response") {
        val json = apiV1Mapper.writeValueAsString(createResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"create\".*")
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

    test("Serialize create response with success") {
        val json = apiV1Mapper.writeValueAsString(createResponseSuccess)
        json shouldMatch Regex(".*\"responseType\":\\s*\"create\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"meaning\":\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldMatch Regex(".*\"value\":\\s*\"о чем-н. не имеющем вкуса, безвкусном \\(разг.\\)\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"t-o-n-y-p\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
        json shouldMatch Regex(".*\"errors\":\\s*\\[\\s*].*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Serialize create response with error") {
        val json = apiV1Mapper.writeValueAsString(createResponseError)
        json shouldMatch Regex(".*\"responseType\":\\s*\"create\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"error\".*")
        json shouldMatch Regex(".*\"meaning\":\\{\\s*}.*")
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

    test("Deserialize empty create response") {
        val json = apiV1Mapper.writeValueAsString(emptyCreateResponse)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningCreateResponse

        val expectedCreateResponseSuccess = emptyCreateResponse.copy(responseType = "create")
        obj shouldBe expectedCreateResponseSuccess
    }

    test("Deserialize create response with success") {
        val json = apiV1Mapper.writeValueAsString(createResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningCreateResponse

        val expectedCreateResponseSuccess = createResponseSuccess.copy(responseType = "create")
        obj shouldBe expectedCreateResponseSuccess
    }

    test("Deserialize create response with error") {
        val json = apiV1Mapper.writeValueAsString(createResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningCreateResponse

        val expectedCreateResponseError = createResponseError.copy(responseType = "create")
        obj shouldBe expectedCreateResponseError
    }

})