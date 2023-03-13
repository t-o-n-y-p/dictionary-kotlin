package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningReadRequestTest : FunSpec ({

    val emptyReadRequest = MeaningReadRequest()
    val firstReadRequest = MeaningReadRequest(
        debug = MeaningDebug(),
        meaning = MeaningReadObject()
    )
    val secondReadRequest = MeaningReadRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD,
            stub = MeaningRequestDebugStubs.CANNOT_READ
        ),
        meaning = MeaningReadObject(
            id = "456"
        )
    )

    test("Serialize empty read request") {
        val json = apiV1Mapper.writeValueAsString(emptyReadRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"read\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"debug\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"id\":"
    }

    test("Serialize read request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstReadRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"read\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*}.*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*}.*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"id\":"
    }

    test("Serialize read request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondReadRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"read\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"mode\":\\s*\"prod\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotRead\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
    }

    test("Deserialize empty read request") {
        val json = apiV1Mapper.writeValueAsString(emptyReadRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningReadRequest

        val expectedFirstReadRequest = emptyReadRequest.copy(requestType = "read")
        obj shouldBe expectedFirstReadRequest
    }

    test("Deserialize read request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstReadRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningReadRequest

        val expectedFirstReadRequest = firstReadRequest.copy(requestType = "read")
        obj shouldBe expectedFirstReadRequest
    }

    test("Deserialize read request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondReadRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningReadRequest

        val expectedSecondReadRequest = secondReadRequest.copy(requestType = "read")
        obj shouldBe expectedSecondReadRequest
    }

})