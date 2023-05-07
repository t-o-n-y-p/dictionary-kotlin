package com.tonyp.dictionarykotlin.log.v1

import ch.qos.logback.classic.Logger
import com.tonyp.dictionarykotlin.log.v1.common.IDictionaryLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun dictionaryLogger(logger: Logger): IDictionaryLogWrapper = DictionaryLogWrapper(
    logger = logger,
    loggerId = logger.name,
)

fun dictionaryLogger(clazz: KClass<*>): IDictionaryLogWrapper =
    dictionaryLogger(LoggerFactory.getLogger(clazz.java) as Logger)
fun dictionaryLogger(loggerId: String): IDictionaryLogWrapper =
    dictionaryLogger(LoggerFactory.getLogger(loggerId) as Logger)