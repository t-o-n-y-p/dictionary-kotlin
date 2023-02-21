package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningSearchResponseTest : FunSpec ({

    val searchResponseSuccess = MeaningSearchResponse(
        requestId = "123",
        result = ResponseResult.SUCCESS,
        meanings = listOf(
            MeaningResponseFullObject(
                id = "456",
                word = "трава",
                meaning = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "t-o-n-y-p",
                approved = false
            ),
            MeaningResponseFullObject(
                id = "789",
                word = "обвал",
                meaning = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "unittest",
                approved = true
            )
        )
    )

    val searchResponseError = MeaningSearchResponse(
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

    test("Search response with success") {
        val json = apiV1Mapper.writeValueAsString(searchResponseSuccess)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"result\":\\s*\"success\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"обвал\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\"о чем-н. не имеющем вкуса, безвкусном \\(разг.\\)\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\"снежные глыбы или обломки скал, обрушившиеся с гор\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"t-o-n-y-p\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"unittest\".*")
        json shouldMatch Regex(".*\"approved\":\\s*false.*")
        json shouldMatch Regex(".*\"approved\":\\s*true.*")
        json shouldNotMatch "\"code\":"
        json shouldNotMatch "\"message\":"
    }

    test("Search response with error") {
        val json = apiV1Mapper.writeValueAsString(searchResponseError)
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

})