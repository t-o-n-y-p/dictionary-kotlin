package com.tonyp.dictionarykotlin.log.v1.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class DictionaryLoggerProvider(
    private val provider: (String) -> IDictionaryLogWrapper = { IDictionaryLogWrapper.DEFAULT }
) {
    fun logger(loggerId: String) = provider(loggerId)
    fun logger(clazz: KClass<*>) = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")
    fun logger(function: KFunction<*>) = provider(function.name)
}