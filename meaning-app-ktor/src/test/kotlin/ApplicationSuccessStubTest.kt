import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*

class ApplicationSuccessStubTest : FunSpec ({

    val createRequest = MeaningCreateRequest(
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
    val createResponse = MeaningCreateResponse(
        responseType = "create",
        requestId = "123",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseFullObject(
            id = "456",
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = false
        )
    )

    val deleteRequest = MeaningDeleteRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaning = MeaningDeleteObject(
            id = "123"
        )
    )
    val deleteResponse = MeaningDeleteResponse(
        responseType = "delete",
        requestId = "789",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseDeleteObject(
            id = "456"
        )
    )

    val readRequest = MeaningReadRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        ),
        meaning = MeaningReadObject(
            id = "789"
        )
    )
    val readResponse = MeaningReadResponse(
        responseType = "read",
        requestId = "456",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseFullObject(
            id = "123",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = true
        )
    )

    val updateRequest = MeaningUpdateRequest(
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
    val updateResponse = MeaningUpdateResponse(
        responseType = "update",
        requestId = "789",
        result = ResponseResult.ERROR,
        meaning = MeaningResponseFullObject(
            id = "123",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = true
        )
    )

    val searchRequest = MeaningSearchRequest(
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
    val searchResponse = MeaningSearchResponse(
        responseType = "search",
        requestId = "789",
        result = ResponseResult.ERROR,
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

    test("Create request success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }

            val response = client.post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                setBody(createRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe createResponse
        }
    }

    test("Delete request success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }

            val response = client.post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                setBody(deleteRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponse
        }
    }

    test("Read request success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }

            val response = client.post("/api/v1/meaning/read") {
                contentType(ContentType.Application.Json)
                setBody(readRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningReadResponse shouldBe readResponse
        }
    }

    test("Update request success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }

            val response = client.post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                setBody(updateRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponse
        }
    }

    test("Search request success stub") {
        testApplication {
            val client = createClient {
                install(ContentNegotiation) {
                    jackson {
                        setConfig(apiV1Mapper.serializationConfig)
                        setConfig(apiV1Mapper.deserializationConfig)
                    }
                }
            }

            val response = client.post("/api/v1/meaning/search") {
                contentType(ContentType.Application.Json)
                setBody(searchRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningSearchResponse shouldBe searchResponse
        }
    }

})