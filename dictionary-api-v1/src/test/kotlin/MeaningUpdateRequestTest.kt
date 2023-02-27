package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningUpdateRequestTest : FunSpec ({

    val firstUpdateRequest = MeaningUpdateRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD
        ),
        meaning = MeaningUpdateObject(
            id = "456"
        )
    )
    val secondUpdateRequest = MeaningUpdateRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_UPDATE
        ),
        meaning = MeaningUpdateObject(
            id = "789",
            approved = true
        )
    )

    test("Serialize update request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstUpdateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"update\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"prod\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldNotMatch "\"stub\":"
        json shouldNotMatch "\"approved\":"
    }

    test("Serialize update request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondUpdateRequest)
        json shouldMatch Regex(".*\"requestType\":\\s*\"update\".*")
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotUpdate\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
        json shouldMatch Regex(".*\"approved\":\\s*true.*")
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