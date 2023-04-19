package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.asDictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import io.ktor.websocket.*

fun WebSocketSession.createInitContext(): DictionaryContext = DictionaryContext(
    command = DictionaryCommand.INIT,
    state = DictionaryState.FINISHING,
    webSocketExtensions = extensions.map{ e -> e.asDictionaryExtension() }.toMutableList()
)

fun WebSocketSession.createInitContext(t: Throwable): DictionaryContext = DictionaryContext(
    command = DictionaryCommand.INIT,
    state = DictionaryState.FAILING,
    webSocketExtensions = extensions.map{ e -> e.asDictionaryExtension() }.toMutableList(),
    errors = mutableListOf(t.asDictionaryError())
)
