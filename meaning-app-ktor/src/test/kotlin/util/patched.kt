package util

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.websocket.*
import io.ktor.serialization.*
import io.ktor.util.*
import io.ktor.websocket.*

private const val WEBSOCKET_VERSION = "13"
private const val NONCE_SIZE = 16
private val REQUEST_EXTENSIONS_KEY = AttributeKey<List<WebSocketExtension<*>>>("Websocket extensions")
private const val PERMESSAGE_DEFLATE = "permessage-deflate"

@OptIn(InternalAPI::class)
class PatchedWebSocketContent : ClientUpgradeContent() {
    private val nonce: String = buildString {
        val nonce = generateNonce(NONCE_SIZE)
        append(nonce.encodeBase64())
    }

    override val headers: Headers = HeadersBuilder().apply {
        append(HttpHeaders.Upgrade, "websocket")
        append(HttpHeaders.Connection, "upgrade")

        append(HttpHeaders.SecWebSocketKey, nonce)
        append(HttpHeaders.SecWebSocketVersion, WEBSOCKET_VERSION)
    }.build()

    override fun verify(headers: Headers) {
        val serverAccept = headers[HttpHeaders.SecWebSocketAccept]
            ?: error("Server should specify header ${HttpHeaders.SecWebSocketAccept}")

        val expectedAccept = websocketServerAccept(nonce)
        check(expectedAccept == serverAccept) {
            "Failed to verify server accept header. Expected: $expectedAccept, received: $serverAccept"
        }
    }

    override fun toString(): String = "WebSocketContent"
}

object PatchedWebSockets : HttpClientPlugin<WebSockets.Config, WebSockets> {
    override val key: AttributeKey<WebSockets> = AttributeKey("Websocket")

    override fun prepare(block: WebSockets.Config.() -> Unit): WebSockets {
        return WebSockets()
    }

    @OptIn(InternalAPI::class)
    override fun install(plugin: WebSockets, scope: HttpClient) {
        scope.requestPipeline.intercept(HttpRequestPipeline.Render) {
            if (!context.url.protocol.isWebsocket()) {
                return@intercept
            }

            context.setCapability(WebSocketCapability, Unit)
            context.attributes.put(REQUEST_EXTENSIONS_KEY, listOf(WebSocketDeflateExtension.install {  }))
            context.header(HttpHeaders.SecWebSocketExtensions, PERMESSAGE_DEFLATE)
            proceedWith(PatchedWebSocketContent())
        }

        scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, session) ->
            if (session !is WebSocketSession) {
                return@intercept
            }
            val defaultSession = DefaultWebSocketSession(session)
            val clientSession = DefaultClientWebSocketSession(context, defaultSession)

            val serverExtensions: List<WebSocketExtensionHeader> = context.response
                .headers[HttpHeaders.SecWebSocketExtensions]
                ?.let { parseWebSocketExtensions(it) } ?: emptyList()

            val clientExtensions = context.attributes[REQUEST_EXTENSIONS_KEY]
            val negotiated = clientExtensions.filter { it.clientNegotiation(serverExtensions) }

            clientSession.apply {
                start(negotiated)
            }
            val response = HttpResponseContainer(info, clientSession)
            proceedWith(response)
        }
    }
}