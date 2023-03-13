package com.tonyp.dictionarykotlin.common.models

@JvmInline
value class DictionaryMeaningId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = DictionaryMeaningId("")
    }
}
