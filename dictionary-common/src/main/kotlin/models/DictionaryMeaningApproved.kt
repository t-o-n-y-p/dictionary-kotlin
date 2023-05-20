package com.tonyp.dictionarykotlin.common.models

enum class DictionaryMeaningApproved(
    val boolean: Boolean?
) {

    NONE(null),
    FALSE(false),
    TRUE(true);

    companion object {
        fun fromBoolean(boolean: Boolean?) = when (boolean) {
            false -> FALSE
            true -> TRUE
            null -> NONE
        }
    }

}
