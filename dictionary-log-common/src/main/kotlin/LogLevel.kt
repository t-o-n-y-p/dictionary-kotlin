package com.tonyp.dictionarykotlin.log.v1.common

enum class LogLevel(
    private val levelInt: Int
) {
    ERROR(40),
    WARN(30),
    INFO(20),
    DEBUG(10),
    TRACE(0);

    @Suppress("unused")
    fun toInt(): Int {
        return levelInt
    }
}