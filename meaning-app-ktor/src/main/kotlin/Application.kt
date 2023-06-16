package com.tonyp.dictionarykotlin.meaning.app

import com.tonyp.dictionarykotlin.meaning.app.plugins.initAppSettings
import com.tonyp.dictionarykotlin.meaning.app.plugins.initPlugins
import com.tonyp.dictionarykotlin.meaning.app.v1.v1MeaningApi
import com.tonyp.dictionarykotlin.meaning.app.v1.v1MeaningWebsocket
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused") // Referenced in application.yaml
fun Application.module(
    appSettings: DictionaryAppSettings = initAppSettings(),
    authConfig: DictionaryAuthConfig = DictionaryAuthConfig(environment),
) {
    initPlugins(appSettings, authConfig)

    routing {
        route("api/v1") {
            v1MeaningApi(appSettings)
        }
        route("ws/v1") {
            v1MeaningWebsocket(appSettings)
        }
    }
}
