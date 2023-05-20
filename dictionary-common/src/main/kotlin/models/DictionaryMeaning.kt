package com.tonyp.dictionarykotlin.common.models

data class DictionaryMeaning (
    var id: DictionaryMeaningId = DictionaryMeaningId.NONE,
    var word: String = "",
    var value: String = "",
    var proposedBy: String = "",
    var approved: DictionaryMeaningApproved = DictionaryMeaningApproved.NONE,
    var version: DictionaryMeaningVersion = DictionaryMeaningVersion.NONE
) {

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = DictionaryMeaning()
    }
}
