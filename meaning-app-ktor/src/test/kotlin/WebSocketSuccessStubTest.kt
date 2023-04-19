import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import util.DataProvider.createRequestStubSuccess
import util.DataProvider.createResponseStubSuccess
import util.DataProvider.deleteRequestStubSuccess
import util.DataProvider.deleteResponseStubSuccess
import util.DataProvider.noCreateDeleteNotificationData
import util.DataProvider.noUpdateNotificationData
import util.DataProvider.searchRequestStubSuccess
import util.DataProvider.searchResponseStubSuccess
import util.DataProvider.successfulCreateDeleteNotificationData
import util.DataProvider.successfulCreateNotificationFromUpdateRequestData
import util.DataProvider.successfulDeleteNotificationFromUpdateRequestData
import util.DataProvider.successfulUpdateNotificationData
import util.DataProvider.updateRequestStubSuccess
import util.DataProvider.updateResponseStubSuccess
import util.DataProvider.updateToCreateResponseStubSuccess
import util.DataProvider.updateToDeleteResponseStubSuccess
import util.PatchedWebSockets
import util.post
import util.webSocket

@OptIn(ExperimentalCoroutinesApi::class)
class WebSocketSuccessStubTest : FreeSpec({

    val initResponseWithNoExtensions = MeaningInitResponse(
        responseType = "init",
        result = ResponseResult.ERROR,
        webSocketExtensions = emptyList()
    )
    val initResponseWithExtension = MeaningInitResponse(
        responseType = "init",
        result = ResponseResult.ERROR,
        webSocketExtensions = listOf(MeaningWebSocketExtension.DEFLATE)
    )

    "Init response with no extensions" - {
        testApplication {
            webSocket("/ws/v1/meaning/search") {
                val raw = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(raw.readText(), MeaningInitResponse::class.java)
                response shouldBe initResponseWithNoExtensions
            }
        }
    }

    "Init response with extensions" - {
        testApplication {
            webSocket("/ws/v1/meaning/search", { install(PatchedWebSockets) }) {
                val raw = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(raw.readText(), MeaningInitResponse::class.java)
                response shouldBe initResponseWithExtension
            }
        }
    }

    "Search request success stub" - {
        testApplication {
            webSocket("/ws/v1/meaning/search") {
                // skipping init response
                incoming.receive() as Frame.Text
                send(apiV1Mapper.writeValueAsString(searchRequestStubSuccess))
                val raw = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(raw.readText(), MeaningSearchResponse::class.java)
                response shouldBe searchResponseStubSuccess
            }
        }
    }

    "Successful create notification to websocket" - {
        successfulCreateDeleteNotificationData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/create") {
                            contentType(ContentType.Application.Json)
                            setBody(createRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningCreateResponse::class.java)
                        response shouldBe createResponseStubSuccess
                    }
                }
            }
        }
    }

    "No create notification to websocket" - {
        noCreateDeleteNotificationData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/create") {
                            contentType(ContentType.Application.Json)
                            setBody(createRequestStubSuccess)
                        }

                        incoming.isEmpty shouldBe true
                    }
                }
            }
        }
    }

    "Successful delete notification to websocket" - {
        successfulCreateDeleteNotificationData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/delete") {
                            contentType(ContentType.Application.Json)
                            setBody(deleteRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningDeleteResponse::class.java)
                        response shouldBe deleteResponseStubSuccess
                    }
                }
            }
        }
    }

    "No delete notification to websocket" - {
        noCreateDeleteNotificationData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/delete") {
                            contentType(ContentType.Application.Json)
                            setBody(deleteRequestStubSuccess)
                        }

                        incoming.isEmpty shouldBe true
                    }
                }
            }
        }
    }

    "Successful update notification to websocket" - {
        successfulUpdateNotificationData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/update") {
                            contentType(ContentType.Application.Json)
                            setBody(updateRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningUpdateResponse::class.java)
                        response shouldBe updateResponseStubSuccess
                    }
                }
            }
        }
    }

    "Successful create notification to websocket from update request" - {
        successfulCreateNotificationFromUpdateRequestData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/update") {
                            contentType(ContentType.Application.Json)
                            setBody(updateRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningCreateResponse::class.java)
                        response shouldBe updateToCreateResponseStubSuccess
                    }
                }
            }
        }
    }

    "Successful delete notification to websocket from update request" - {
        successfulDeleteNotificationFromUpdateRequestData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/update") {
                            contentType(ContentType.Application.Json)
                            setBody(updateRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningDeleteResponse::class.java)
                        response shouldBe updateToDeleteResponseStubSuccess
                    }
                }
            }
        }
    }

    "No update notification to websocket" - {
        noUpdateNotificationData.map { (description, searchRequest) ->
            description {
                testApplication {
                    webSocket("/ws/v1/meaning/search") {
                        // skipping init response
                        incoming.receive() as Frame.Text
                        send(apiV1Mapper.writeValueAsString(searchRequest))
                        // skipping search response
                        incoming.receive() as Frame.Text

                        post("/api/v1/meaning/update") {
                            contentType(ContentType.Application.Json)
                            setBody(updateRequestStubSuccess)
                        }

                        incoming.isEmpty shouldBe true
                    }
                }
            }
        }
    }

})