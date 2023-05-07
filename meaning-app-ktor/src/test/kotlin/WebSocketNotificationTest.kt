import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningUpdateResponse
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import util.DataProvider
import util.post
import util.webSocket

@OptIn(ExperimentalCoroutinesApi::class)
class WebSocketNotificationTest : FreeSpec({

    "Successful create notification to websocket" - {
        DataProvider.successfulCreateDeleteNotificationData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.createRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningCreateResponse::class.java)
                        response shouldBe DataProvider.createResponseStubSuccess
                    }
                }
            }
        }
    }

    "No create notification to websocket" - {
        DataProvider.noCreateDeleteNotificationData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.createRequestStubSuccess)
                        }

                        incoming.isEmpty shouldBe true
                    }
                }
            }
        }
    }

    "Successful delete notification to websocket" - {
        DataProvider.successfulCreateDeleteNotificationData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.deleteRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningDeleteResponse::class.java)
                        response shouldBe DataProvider.deleteResponseStubSuccess
                    }
                }
            }
        }
    }

    "No delete notification to websocket" - {
        DataProvider.noCreateDeleteNotificationData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.deleteRequestStubSuccess)
                        }

                        incoming.isEmpty shouldBe true
                    }
                }
            }
        }
    }

    "Successful update notification to websocket" - {
        DataProvider.successfulUpdateNotificationData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.updateRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningUpdateResponse::class.java)
                        response shouldBe DataProvider.updateResponseStubSuccess
                    }
                }
            }
        }
    }

    "Successful create notification to websocket from update request" - {
        DataProvider.successfulCreateNotificationFromUpdateRequestData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.updateRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningCreateResponse::class.java)
                        response shouldBe DataProvider.updateToCreateResponseStubSuccess
                    }
                }
            }
        }
    }

    "Successful delete notification to websocket from update request" - {
        DataProvider.successfulDeleteNotificationFromUpdateRequestData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.updateRequestStubSuccess)
                        }

                        val raw = incoming.receive() as Frame.Text
                        val response = apiV1Mapper.readValue(raw.readText(), MeaningDeleteResponse::class.java)
                        response shouldBe DataProvider.updateToDeleteResponseStubSuccess
                    }
                }
            }
        }
    }

    "No update notification to websocket" - {
        DataProvider.noUpdateNotificationData.map { (description, searchRequest) ->
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
                            setBody(DataProvider.updateRequestStubSuccess)
                        }

                        incoming.isEmpty shouldBe true
                    }
                }
            }
        }
    }

})