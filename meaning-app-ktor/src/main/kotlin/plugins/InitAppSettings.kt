package com.tonyp.dictionarykotlin.meaning.app.plugins

import com.tonyp.dictionarykotlin.api.v1.apiV1Mapper
import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.DictionaryCorSettings
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.log.v1.DictionaryLogWrapper
import com.tonyp.dictionarykotlin.log.v1.common.DictionaryLoggerProvider
import com.tonyp.dictionarykotlin.log.v1.dictionaryLogger
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import com.tonyp.dictionarykotlin.meaning.app.module
import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.stub.MeaningRepoStub
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
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
        repositories = mapOf(
            DictionaryWorkMode.PROD to MeaningRepoInMemory(),
            DictionaryWorkMode.TEST to MeaningRepoInMemory(),
            DictionaryWorkMode.STUB to MeaningRepoStub(),
        )
    )
    return DictionaryAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = DictionaryMeaningProcessor(corSettings)
    )
}

fun Application.initPlugins(appSettings: DictionaryAppSettings) {
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
}