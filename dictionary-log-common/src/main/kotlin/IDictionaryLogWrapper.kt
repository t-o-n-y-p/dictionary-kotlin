package com.tonyp.dictionarykotlin.log.v1.common

import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

interface IDictionaryLogWrapper {

    val loggerId: String

    fun log(
        msg: String = "",
        level: LogLevel = LogLevel.TRACE,
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objects: Map<String, Any>? = null,
    )

    fun error(
        msg: String = "",
        marker: String = "DEV",
        e: Throwable? = null,
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) = log(msg, LogLevel.ERROR, marker, e, data, objects)

    fun info(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) = log(msg, LogLevel.INFO, marker, null, data, objects)

    fun debug(
        msg: String = "",
        marker: String = "DEV",
        data: Any? = null,
        objects: Map<String, Any>? = null,
    ) = log(msg, LogLevel.DEBUG, marker, null, data, objects)

    /**
     * Функция обертка для выполнения прикладного кода с логированием перед выполнением и после
     */
    @OptIn(ExperimentalTime::class)
    suspend fun <T> doWithLogging(
        id: String = "",
        level: LogLevel = LogLevel.INFO,
        block: suspend () -> T,
    ): T = try {
        log("Started $loggerId $id", level)
        val (res, diffTime) = measureTimedValue { block() }

        log(
            msg = "Finished $loggerId $id",
            level = level,
            objects = mapOf("metricHandleTime" to diffTime.toIsoString())
        )
        res
    } catch (e: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = LogLevel.ERROR,
            e = e
        )
        throw e
    }

    /**
     * Функция обертка для выполнения прикладного кода с логированием ошибки
     */
    suspend fun <T> doWithErrorLogging(
        id: String = "",
        throwRequired: Boolean = true,
        block: suspend () -> T,
    ): T? = try {
        val result = block()
        result
    } catch (e: Throwable) {
        log(
            msg = "Failed $loggerId $id",
            level = LogLevel.ERROR,
            e = e
        )
        if (throwRequired) throw e else null
    }

    companion object {
        val DEFAULT = object: IDictionaryLogWrapper {
            override val loggerId: String = "DEFAULT"

            override fun log(
                msg: String,
                level: LogLevel,
                marker: String,
                e: Throwable?,
                data: Any?,
                objects: Map<String, Any>?,
            ) {
                val markerString = marker
                    .takeIf { it.isNotBlank() }
                    ?.let { " ($it)" }
                val args = listOfNotNull(
                    "${Clock.System.now()} [${level.name}]$markerString: $msg",
                    e?.let { "${it.message ?: "Unknown reason"}:\n${it.stackTraceToString()}" },
                    data.toString(),
                    objects.toString(),
                )
                println(args.joinToString("\n"))
            }

        }
    }

}