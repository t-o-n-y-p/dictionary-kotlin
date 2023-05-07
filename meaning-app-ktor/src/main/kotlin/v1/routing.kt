package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Route.v1MeaningApi(appSettings: DictionaryAppSettings) {
    val logger = appSettings.corSettings.loggerProvider.logger(Route::v1MeaningApi)
    route("meaning") {
        post("create") {
            call.createMeaning(appSettings, logger)
        }
        post("read") {
            call.readMeaning(appSettings, logger)
        }
        post("update") {
            call.updateMeaning(appSettings, logger)
        }
        post("delete") {
            call.deleteMeaning(appSettings, logger)
        }
        post("search") {
            call.searchMeaning(appSettings, logger)
        }
    }
}

fun Route.v1MeaningWebsocket(appSettings: DictionaryAppSettings) {
    val logger = appSettings.corSettings.loggerProvider.logger(Route::v1MeaningWebsocket)
    route("meaning") {
        webSocket("search") {
            searchMeaning(appSettings, logger)
        }
    }
}