package util

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import kotlinx.coroutines.withTimeout

suspend fun ApplicationTestBuilder.post(
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val client = createClient {
        install(ContentNegotiation) {
            jackson {
                setConfig(apiV1Mapper.serializationConfig)
                setConfig(apiV1Mapper.deserializationConfig)
            }
        }
    }
    return client.post(urlString) {
        block()
    }
}

suspend fun ApplicationTestBuilder.webSocket(
    urlString: String,
    config: HttpClientConfig<out HttpClientEngineConfig>.() -> Unit = {
        install(WebSockets)
    },
    block: suspend DefaultClientWebSocketSession.() -> Unit = {}
) {
    val client = createClient {
        config()
    }
    client.webSocket(urlString) {
        withTimeout(3000) {
            block()
        }
    }
}