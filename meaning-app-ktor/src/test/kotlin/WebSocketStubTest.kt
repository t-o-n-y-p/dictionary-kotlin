import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.*
import io.ktor.websocket.*
import util.DataProvider.searchRequestStubError
import util.DataProvider.searchRequestStubSuccess
import util.DataProvider.searchResponseStubError
import util.DataProvider.searchResponseStubSuccess
import util.PatchedWebSockets
import util.webSocket

class WebSocketStubTest : FreeSpec({

    val initResponseWithNoExtensions = MeaningInitResponse(
        responseType = "init",
        result = ResponseResult.SUCCESS,
        webSocketExtensions = emptyList()
    )
    val initResponseWithExtension = MeaningInitResponse(
        responseType = "init",
        result = ResponseResult.SUCCESS,
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

    "Search request error stub" - {
        testApplication {
            webSocket("/ws/v1/meaning/search") {
                // skipping init response
                incoming.receive() as Frame.Text
                send(apiV1Mapper.writeValueAsString(searchRequestStubError))
                val raw = incoming.receive() as Frame.Text
                val response = apiV1Mapper.readValue(raw.readText(), MeaningSearchResponse::class.java)
                response shouldBe searchResponseStubError
            }
        }
    }

})