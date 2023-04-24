package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import io.ktor.websocket.*
import kotlinx.datetime.Clock

fun WebSocketSession.createInitContext(): DictionaryContext = DictionaryContext(
    timeStart = Clock.System.now(),
    command = DictionaryCommand.INIT,
    state = DictionaryState.FINISHING,
    webSocketExtensions = extensions.map{ e -> e.asDictionaryExtension() }.toMutableList()
)
