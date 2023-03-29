package com.tonyp.dictionarykotlin.meaning.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*

fun Route.v1MeaningApi() {
    route("meaning") {
        post("create") {
            call.createMeaning()
        }
        post("read") {
            call.readMeaning()
        }
        post("update") {
            call.updateMeaning()
        }
        post("delete") {
            call.deleteMeaning()
        }
        post("search") {
            call.searchMeaning()
        }
    }
}

fun Route.v1MeaningWebsocket() {
    route("meaning") {
        webSocket("search") {
            searchMeaning()
        }
    }
}