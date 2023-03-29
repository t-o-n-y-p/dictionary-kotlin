package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import io.ktor.websocket.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class WebSocketSessionMap(
    private val sessions: MutableMap<WebSocketSession, DictionaryContext> = mutableMapOf(),
    private val mutex: Mutex = Mutex()
) {
    val entries
        get() = sessions.entries

    suspend fun put(k: WebSocketSession, v: DictionaryContext) = mutex.withLock {
        sessions[k] = v
    }

    suspend fun remove(k: WebSocketSession) = mutex.withLock {
        sessions.remove(k)
    }

    suspend fun clear() = mutex.withLock {
        sessions.clear()
    }
}