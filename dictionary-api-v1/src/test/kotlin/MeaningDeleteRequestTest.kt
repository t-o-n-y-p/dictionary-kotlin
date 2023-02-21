package com.tonyp.dictionarykotlin.api.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotMatch

class MeaningDeleteRequestTest : FunSpec ({

    val firstDeleteRequest = MeaningDeleteRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD
        ),
        meaning = MeaningDeleteObject(
            id = "456"
        )
    )
    val secondDeleteRequest = MeaningDeleteRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_DELETE
        ),
        meaning = MeaningDeleteObject(
            id = "789"
        )
    )

    test("Delete request with missing fields") {
        val json = apiV1Mapper.writeValueAsString(firstDeleteRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"123\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"prod\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"456\".*")
        json shouldNotMatch "\"stub\":"
    }

    test("Delete request with all fields") {
        val json = apiV1Mapper.writeValueAsString(secondDeleteRequest)
        json shouldMatch Regex(".*\"requestId\":\\s*\"456\".*")
        json shouldMatch Regex(".*\"mode\":\\s*\"stub\".*")
        json shouldMatch Regex(".*\"stub\":\\s*\"cannotDelete\".*")
        json shouldMatch Regex(".*\"id\":\\s*\"789\".*")
    }

})