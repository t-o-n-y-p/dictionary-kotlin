package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
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

    test("Read request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstReadRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"test\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldNotMatch "\"stub\":"
    }

    test("Read request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondReadRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotRead\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
    }

})