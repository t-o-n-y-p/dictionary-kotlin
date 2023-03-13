package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningDeleteRequestTest : FunSpec ({

    val emptyDeleteRequest = MeaningDeleteRequest()
    val firstDeleteRequest = MeaningDeleteRequest(
        debug = MeaningDebug(),
        meaning = MeaningDeleteObject()
    )
    val secondDeleteRequest = MeaningDeleteRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.TEST,
            stub = MeaningRequestDebugStubs.CANNOT_DELETE
        ),
        meaning = MeaningDeleteObject(
            id = "789"
        )
    )

    test("Serialize empty delete request") {
        val json = apiV1Mapper.writeValueAsString(emptyDeleteRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"delete\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"debug\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"id\":"
    }

    test("Serialize delete request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstDeleteRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"delete\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*}.*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*}.*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"id\":"
    }

    test("Serialize delete request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondDeleteRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"delete\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"mode\":\\s*\"test\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotDelete\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
    }

    test("Deserialize empty delete request") {
        val json = apiV1Mapper.writeValueAsString(emptyDeleteRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningDeleteRequest

        val expectedFirstDeleteRequest = emptyDeleteRequest.copy(requestType = "delete")
        obj shouldBe expectedFirstDeleteRequest
    }

    test("Deserialize delete request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstDeleteRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningDeleteRequest

        val expectedFirstDeleteRequest = firstDeleteRequest.copy(requestType = "delete")
        obj shouldBe expectedFirstDeleteRequest
    }

    test("Deserialize delete request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondDeleteRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningDeleteRequest

        val expectedSecondDeleteRequest = secondDeleteRequest.copy(requestType = "delete")
        obj shouldBe expectedSecondDeleteRequest
    }

})