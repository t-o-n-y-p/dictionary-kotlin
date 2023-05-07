package com.tonyp.dictionarykotlin.log.v1

import ch.qos.logback.classic.Logger
import com.tonyp.dictionarykotlin.log.v1.common.IDictionaryLogWrapper
import com.tonyp.dictionarykotlin.log.v1.common.LogLevel
import kotlinx.datetime.Clock
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Marker
import org.slf4j.event.KeyValuePair
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent

class DictionaryLogWrapper (
    val logger: Logger,
    override val loggerId: String = "",
) : IDictionaryLogWrapper {
    private fun log(
        msg: String = "",
        level: Level = Level.TRACE,
        marker: Marker = DefaultMarker("DEV"),
        e: Throwable? = null,
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) {
        logger.log(object : LoggingEvent {
            override fun getThrowable() = e
            override fun getTimeStamp(): Long = Clock.System.now().toEpochMilliseconds()
            override fun getThreadName(): String = Thread.currentThread().name
            override fun getMessage(): String = msg
            override fun getArguments(): MutableList<Any> = argumentArray.toMutableList()
            override fun getArgumentArray(): Array<out Any> = data
                ?.let { d ->
                    listOfNotNull(
                        objects?.map { StructuredArguments.keyValue(it.key, it.value) }?.toTypedArray(),
                        StructuredArguments.keyValue("data", d),
                    ).toTypedArray()
                }
                ?: objects?.mapNotNull { StructuredArguments.keyValue(it.key, it.value) }?.toTypedArray()
                ?: emptyArray()

            override fun getMarkers(): MutableList<Marker> = mutableListOf(marker)
            override fun getKeyValuePairs(): MutableList<KeyValuePair> = objects
                ?.mapNotNull {
                    it.let { KeyValuePair(it.key, it.value) }
                }
                ?.toMutableList()
                ?: mutableListOf()

            override fun getLevel(): Level = level
            override fun getLoggerName(): String = logger.name
        })
    }

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objects: Map<String, Any>?,
    ) = log(
        msg = msg,
        level = level.toLogback(),
        marker = DefaultMarker(marker),
        e = e,
        data = data,
        objects = objects,
    )

    private fun LogLevel.toLogback() = when (this) {
        LogLevel.ERROR -> Level.ERROR
        LogLevel.WARN -> Level.WARN
        LogLevel.INFO -> Level.INFO
        LogLevel.DEBUG -> Level.DEBUG
        LogLevel.TRACE -> Level.TRACE
    }
}
