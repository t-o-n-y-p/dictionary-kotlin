package com.tonyp.dictionarykotlin.common.models

@JvmInline
value class DictionaryMeaningVersion(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = DictionaryMeaningVersion("")
    }

}