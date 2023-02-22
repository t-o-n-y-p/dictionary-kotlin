package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningUpdateResponseTest : FunSpec ({

    val updateResponseSuccess = MeaningUpdateResponse(
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

    val updateResponseError = MeaningUpdateResponse(
        requestId = "456",
        result = ResponseResult.ERROR,
        errors = mutableListOf(
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

    test("Serialize update response with success") {
        val json = apiV1Mapper.writeValueAsString(updateResponseSuccess)
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

    test("Serialize update response with error") {
        val json = apiV1Mapper.writeValueAsString(updateResponseError)
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

    test("Deserialize update response with success") {
        val json = apiV1Mapper.writeValueAsString(updateResponseSuccess)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningUpdateResponse

        val expectedUpdateResponseSuccess = updateResponseSuccess.copy(responseType = "update")
        obj shouldBe expectedUpdateResponseSuccess
    }

    test("Deserialize update response with error") {
        val json = apiV1Mapper.writeValueAsString(updateResponseError)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as MeaningUpdateResponse

        val expectedUpdateResponseError = updateResponseError.copy(responseType = "update")
        obj shouldBe expectedUpdateResponseError
    }

})