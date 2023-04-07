package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Route.v1MeaningApi(appSettings: DictionaryAppSettings) {
    route("meaning") {
        post("create") {
            call.createMeaning(appSettings)
        }
        post("read") {
            call.readMeaning(appSettings)
        }
        post("update") {
            call.updateMeaning(appSettings)
        }
        post("delete") {
            call.deleteMeaning(appSettings)
        }
        post("search") {
            call.searchMeaning(appSettings)
        }
    }
}

fun Route.v1MeaningWebsocket(appSettings: DictionaryAppSettings) {
    route("meaning") {
        webSocket("search") {
            searchMeaning(appSettings)
        }
    }
}