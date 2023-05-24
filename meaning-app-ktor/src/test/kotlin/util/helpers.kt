package util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.common.DictionaryCorSettings
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig.Companion.GROUPS_CLAIM
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig.Companion.NAME_CLAIM
import com.tonyp.dictionarykotlin.meaning.app.module
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.jackson.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.coroutines.withTimeout
import java.util.*

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

@KtorDsl
fun testApplication(
    repository: IMeaningRepository,
    principal: DictionaryPrincipal = DictionaryPrincipal(
        name = "t_o_n_y_p",
        groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
    ),
    block: suspend ApplicationTestBuilder.(String) -> Unit
) {
    val testConfig = ApplicationConfig("application-test.yaml")
    testApplication {
        environment {
            config = testConfig
        }
        application {
            module(
                DictionaryAppSettings(
                    corSettings = DictionaryCorSettings(
                        repositories = DictionaryWorkMode.values().associateWith { repository }
                    )
                )
            )
        }
        val authConfig = DictionaryAuthConfig(config = testConfig)
        block(
            JWT.create()
                .withAudience(authConfig.audience)
                .withIssuer(authConfig.issuer)
                .withClaim(NAME_CLAIM, principal.name)
                .withClaim(GROUPS_CLAIM, principal.groups.map { it.name })
                .withExpiresAt(Date(System.currentTimeMillis() + 120_000))
                .sign(Algorithm.HMAC256(authConfig.secret))
        )
    }
}