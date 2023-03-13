package com.tonyp.dictionarykotlin.meaning.app.v1

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.v1Meaning() {
    route("ad") {
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