package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningUpdateRequestTest : FunSpec ({

    val emptyUpdateRequest = MeaningUpdateRequest()
    val firstUpdateRequest = MeaningUpdateRequest(
        debug = MeaningDebug(),
        meaning = MeaningUpdateObject()
    )
    val secondUpdateRequest = MeaningUpdateRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD,
            stub = MeaningRequestDebugStubs.CANNOT_UPDATE
        ),
        meaning = MeaningUpdateObject(
            id = "789",
            approved = true
        )
    )

    test("Serialize empty update request") {
        val json = apiV1Mapper.writeValueAsString(emptyUpdateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"update\".*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"debug\":"
        json shouldNotMatch "\"meaning\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Serialize update request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstUpdateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"update\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*}.*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*}.*")
        json shouldNotMatch "\"requestId\":"
        json shouldNotMatch "\"mode\":"
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"id\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Serialize update request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondUpdateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"update\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"debug\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"mode\":\\s*\"prod\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotUpdate\".*")
        json shouldMatch Regex(".*\"meaning\":\\s*\\{\\s*[^}].*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"approved\":\\s*true.*")
    }

    test("Deserialize empty update request") {
        val json = apiV1Mapper.writeValueAsString(emptyUpdateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningUpdateRequest

        val expectedFirstUpdateRequest = emptyUpdateRequest.copy(requestType = "update")
        obj shouldBe expectedFirstUpdateRequest
    }

    test("Deserialize update request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstUpdateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningUpdateRequest

        val expectedFirstUpdateRequest = firstUpdateRequest.copy(requestType = "update")
        obj shouldBe expectedFirstUpdateRequest
    }

    test("Deserialize update request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondUpdateRequest)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as MeaningUpdateRequest

        val expectedSecondUpdateRequest = secondUpdateRequest.copy(requestType = "update")
        obj shouldBe expectedSecondUpdateRequest
    }

})