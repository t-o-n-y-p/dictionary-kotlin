package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningCreateRequestTest : FunSpec ({

    val firstCreateRequest = MeaningCreateRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.TEST
        ),
        meaning = MeaningCreateObject(
            word = "трава",
            meaning = "о чем-н. не имеющем вкуса, безвкусном (разг.)"
        )
    )
    val secondCreateRequest = MeaningCreateRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_CREATE
        ),
        meaning = MeaningCreateObject(
            word = "обвал",
            meaning = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p"
        )
    )

    test("Create request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstCreateRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"test\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"трава\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\"о чем-н. не имеющем вкуса, безвкусном \\(разг.\\)\".*")
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"proposedBy\":"
    }

    test("Create request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondCreateRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotCreate\".*")
        json shouldMatch Regex(".*\"word\":\\s*\"обвал\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\"снежные глыбы или обломки скал, обрушившиеся с гор\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"t-o-n-y-p\".*")
    }

})