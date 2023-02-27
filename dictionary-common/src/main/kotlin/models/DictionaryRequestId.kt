package com.tonyp.dictionarykotlin.common.models

@JvmInline
value class DictionaryRequestId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = DictionaryRequestId("")
    }
}
