package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningCreateRequestTest : FunSpec ({

    val emptyCreateRequest = MeaningCreateRequest()
    val firstCreateRequest = MeaningCreateRequest(
        debug = MeaningDebug(),
        meaning = MeaningCreateObject()
    )
    val secondCreateRequest = MeaningCreateRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_CREATE
        ),
        meaning = MeaningCreateObject(
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p"
        )
    )

    test("Serialize empty create request") {
        val json = apiV1Mapper.writeValueAsString(emptyCreateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"create\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"debug\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
    }

    test("Serialize create request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstCreateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"create\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*}.*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*}.*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"word\":"
        json shouldNotMatch "\"value\":"
        json shouldNotMatch "\"proposedBy\":"
    }

    test("Serialize create request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondCreateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"create\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotCreate\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"word\":\\s*\"обвал\".*")
        json shouldMatch Regex(".*\"value\":\\s*\"снежные глыбы или обломки скал, обрушившиеся с гор\".*")
        json shouldMatch Regex(".*\"proposedBy\":\\s*\"t-o-n-y-p\".*")
    }

    test("Deserialize empty create request") {
        val json = apiV1Mapper.writeValueAsString(emptyCreateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningCreateRequest

        val expectedFirstCreateRequest = emptyCreateRequest.copy(requestType = "create")
        obj shouldBe expectedFirstCreateRequest
    }


    test("Deserialize create request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstCreateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningCreateRequest

        val expectedFirstCreateRequest = firstCreateRequest.copy(requestType = "create")
        obj shouldBe expectedFirstCreateRequest
    }

    test("Deserialize create request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondCreateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningCreateRequest

        val expectedSecondCreateRequest = secondCreateRequest.copy(requestType = "create")
        obj shouldBe expectedSecondCreateRequest
    }

})