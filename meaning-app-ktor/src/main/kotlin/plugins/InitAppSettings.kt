package com.tonyp.dictionarykotlin.meaning.app.plugins

import com.auth0.jwt.JWT
import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.DictionaryCorSettings
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.log.v1.DictionaryLogWrapper
import com.tonyp.dictionarykotlin.log.v1.common.DictionaryLoggerProvider
import com.tonyp.dictionarykotlin.log.v1.dictionaryLogger
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig.Companion.GROUPS_CLAIM
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig.Companion.NAME_CLAIM
import com.tonyp.dictionarykotlin.meaning.app.base.resolveAlgorithm
import com.tonyp.dictionarykotlin.meaning.app.module
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.slf4j.event.Level

fun Application.initAppSettings(): DictionaryAppSettings {
    val corSettings = DictionaryCorSettings(
        loggerProvider = DictionaryLoggerProvider { dictionaryLogger(it) },
        repositories = DictionaryWorkMode.values().associateWith { getRepository(it) }
    )
    return DictionaryAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = DictionaryMeaningProcessor(corSettings)
    )
}

fun Application.initPlugins(
    appSettings: DictionaryAppSettings,
    authConfig: DictionaryAuthConfig
) {
    val initPluginsLogger = appSettings.corSettings.loggerProvider.logger(Application::initPlugins)
    install(CachingHeaders)
    install(DefaultHeaders)
    install(AutoHeadResponse)
    install(WebSockets) {
        extensions {
            install(WebSocketDeflateExtension)
        }
    }

    install(CORS) {
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        appSettings.appUrls.forEach {
            val split = it.split("://")
            println("$split")
            when (split.size) {
                2 -> allowHost(
                    split[1].split("/")[0],
                    listOf(split[0])
                )
                1 -> allowHost(
                    split[0].split("/")[0],
                    listOf("http", "https")
                )
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            setConfig(apiV1Mapper.serializationConfig)
            setConfig(apiV1Mapper.deserializationConfig)
        }
    }

    install(CallLogging) {
        level = Level.INFO
        val wrapper = appSettings
            .corSettings
            .loggerProvider
            .logger(Application::module) as? DictionaryLogWrapper
        wrapper?.logger?.also { logger = it }
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = authConfig.realm

            verifier {
                val algorithm = it.resolveAlgorithm(authConfig)
                JWT
                    .require(algorithm)
                    .withAudience(authConfig.audience)
                    .withIssuer(authConfig.issuer)
                    .build()
            }
            validate { jwtCredential: JWTCredential ->
                when {
                    jwtCredential.payload.getClaim(NAME_CLAIM).asString().isNullOrBlank() -> {
                        initPluginsLogger.error("Name claim must not be empty in JWT token")
                        null
                    }
                    jwtCredential.payload.getClaim(GROUPS_CLAIM).asList(String::class.java).isNullOrEmpty() -> {
                        initPluginsLogger.error("Groups claim must not be empty in JWT token")
                        null
                    }
                    else -> JWTPrincipal(jwtCredential.payload)
                }
            }
        }
    }
}