package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningReadRequestTest : FunSpec ({

    val firstReadRequest = MeaningReadRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.TEST
        ),
        meaning = MeaningReadObject(
            id = "456"
        )
    )
    val secondReadRequest = MeaningReadRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_READ
        ),
        meaning = MeaningReadObject(
            id = "789"
        )
    )

    test("Serialize read request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstReadRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"test\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldNotMatch "\"stub\":"
    }

    test("Serialize read request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondReadRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotRead\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
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