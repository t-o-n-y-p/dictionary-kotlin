package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.common.models.DictionaryWebSocketExtension
import io.ktor.websocket.*

fun WebSocketExtension<*>.asDictionaryExtension() = when (this) {
    is WebSocketDeflateExtension -> DictionaryWebSocketExtension.DEFLATE
    else -> DictionaryWebSocketExtension.NONE
}