package util

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.stubs.StubErrorCode
import io.kotest.data.row

object DataProvider {

    val createRequestStubSuccess = MeaningCreateRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaning = MeaningCreateObject(
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest"
        )
    )
    val createResponseStubSuccess = MeaningCreateResponse(
        responseType = "create",
        requestId = "123",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "456",
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = false
        )
    )

    val createRequestStubError = MeaningCreateRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_CREATE
        ),
        meaning = MeaningCreateObject(
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest"
        )
    )
    val createResponseStubError = MeaningCreateResponse(
        responseType = "create",
        requestId = "123",
        result = ResponseResult.ERROR,
        errors = listOf(
            Error(code = StubErrorCode.CANNOT_CREATE.name, message = "Cannot create")
        ),
        meaning = MeaningResponseFullObject()
    )

    val deleteRequestStubSuccess = MeaningDeleteRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaning = MeaningDeleteObject(
            id = "123"
        )
    )
    val deleteResponseStubSuccess = MeaningDeleteResponse(
        responseType = "delete",
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseDeleteObject(
            id = "456"
        )
    )

    val deleteRequestStubError = MeaningDeleteRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_DELETE
        ),
        meaning = MeaningDeleteObject(
            id = "123"
        )
    )
    val deleteResponseStubError = MeaningDeleteResponse(
        responseType = "delete",
        requestId = "789",
        result = ResponseResult.ERROR,
        errors = listOf(
            Error(code = StubErrorCode.CANNOT_DELETE.name, message = "Cannot delete")
        ),
        meaning = MeaningResponseDeleteObject()
    )

    val readRequestStubSuccess = MeaningReadRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaning = MeaningReadObject(
            id = "789"
        )
    )
    val readResponseStubSuccess = MeaningReadResponse(
        responseType = "read",
        requestId = "456",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "123",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = true
        )
    )

    val readRequestStubError = MeaningReadRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_READ
        ),
        meaning = MeaningReadObject(
            id = "789"
        )
    )
    val readResponseStubError = MeaningReadResponse(
        responseType = "read",
        requestId = "456",
        result = ResponseResult.ERROR,
        errors = listOf(
            Error(code = StubErrorCode.CANNOT_READ.name, message = "Cannot read")
        ),
        meaning = MeaningResponseFullObject()
    )

    val updateRequestStubSuccess = MeaningUpdateRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaning = MeaningUpdateObject(
            id = "456",
            approved = false
        )
    )
    val updateResponseStubSuccess = MeaningUpdateResponse(
        responseType = "update",
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "123",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = true
        )
    )
    val updateToCreateResponseStubSuccess = MeaningCreateResponse(
        responseType = "create",
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseFullObject(
            id = "123",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = true
        )
    )
    val updateToDeleteResponseStubSuccess = MeaningDeleteResponse(
        responseType = "delete",
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meaning = MeaningResponseDeleteObject(
            id = "123"
        )
    )

    val updateRequestStubError = MeaningUpdateRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_UPDATE
        ),
        meaning = MeaningUpdateObject(
            id = "456",
            approved = false
        )
    )
    val updateResponseStubError = MeaningUpdateResponse(
        responseType = "update",
        requestId = "789",
        result = ResponseResult.ERROR,
        errors = listOf(
            Error(code = StubErrorCode.CANNOT_UPDATE.name, message = "Cannot update")
        ),
        meaning = MeaningResponseFullObject()
    )

    val searchRequestStubSuccess = MeaningSearchRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaningFilter = MeaningSearchFilter(
            word = "растение",
            approved = false
        )
    )
    val searchResponseStubSuccess = MeaningSearchResponse(
        responseType = "search",
        requestId = "789",
        result = ResponseResult.SUCCESS,
        meanings = listOf(
            MeaningResponseFullObject(
                id = "123",
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest",
                approved = true
            ),
            MeaningResponseFullObject(
                id = "456",
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = false
            )
        )
    )

    val searchRequestStubError = MeaningSearchRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.CANNOT_SEARCH
        ),
        meaningFilter = MeaningSearchFilter(
            word = "растение",
            approved = false
        )
    )
    val searchResponseStubError = MeaningSearchResponse(
        responseType = "search",
        requestId = "789",
        result = ResponseResult.ERROR,
        errors = listOf(
            Error(code = StubErrorCode.CANNOT_SEARCH.name, message = "Cannot search")
        )
    )

    val successfulCreateDeleteNotificationData = listOf(
        row(
            "Empty word and empty approved",
            searchRequest {
                meaningFilter {}
            }
        ),
        row(
            "Word and approved",
            searchRequest {
                meaningFilter {
                    word = "обвал"
                    approved = false
                }
            }
        ),
        row(
            "Word and empty approved",
            searchRequest {
                meaningFilter {
                    word = "обвал"
                }
            }
        ),
        row(
            "Empty word and approved",
            searchRequest {
                meaningFilter {
                    approved = false
                }
            }
        )
    )

    val noCreateDeleteNotificationData = listOf(
        row(
            "Empty word and wrong approved",
            searchRequest {
                meaningFilter {
                    approved = true
                }
            }
        ),
        row(
            "Wrong word and empty approved",
            searchRequest {
                meaningFilter {
                    word = "трава"
                }
            }
        ),
        row(
            "Word and wrong approved",
            searchRequest {
                meaningFilter {
                    word = "обвал"
                    approved = true
                }
            }
        ),
        row(
            "Wrong word and approved",
            searchRequest {
                meaningFilter {
                    word = "трава"
                    approved = false
                }
            }
        ),
        row(
            "Wrong word and wrong approved",
            searchRequest {
                meaningFilter {
                    word = "трава"
                    approved = true
                }
            }
        )
    )
    val successfulUpdateNotificationData = listOf(
        row(
            "Empty word and empty approved",
            searchRequest {
                meaningFilter {}
            }
        ),
        row(
            "Word and empty approved",
            searchRequest {
                meaningFilter {
                    word = "трава"
                }
            }
        )
    )
    val successfulCreateNotificationFromUpdateRequestData = listOf(
        row(
            "Empty word and approved",
            searchRequest {
                meaningFilter {
                    approved = true
                }
            }
        ),
        row(
            "Word and approved",
            searchRequest {
                meaningFilter {
                    word = "трава"
                    approved = true
                }
            }
        )
    )
    val successfulDeleteNotificationFromUpdateRequestData = listOf(
        row(
            "Empty word and wrong approved",
            searchRequest {
                meaningFilter {
                    approved = false
                }
            }
        ),
        row(
            "Word and wrong approved",
            searchRequest {
                meaningFilter {
                    word = "трава"
                    approved = false
                }
            }
        )
    )
    val noUpdateNotificationData = listOf(
        row(
            "Wrong word and wrong approved",
            searchRequest {
                meaningFilter {
                    word = "обвал"
                    approved = false
                }
            }
        ),
        row(
            "Wrong word and approved",
            searchRequest {
                meaningFilter {
                    word = "обвал"
                    approved = true
                }
            }
        ),
        row(
            "Wrong word and empty approved",
            searchRequest {
                meaningFilter {
                    word = "обвал"
                }
            }
        )
    )
}



