package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.asDictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import io.ktor.websocket.*

fun WebSocketSession.createInitContext(): DictionaryContext {
    val context = DictionaryContext()
    context.command = DictionaryCommand.INIT
    context.webSocketExtensions.addAll(extensions.map{ e -> e.asDictionaryExtension() }.toList())
    return context
}

fun WebSocketSession.createInitContext(t: Throwable): DictionaryContext {
    val context = createInitContext()
    context.errors.add(t.asDictionaryError())
    return context
}