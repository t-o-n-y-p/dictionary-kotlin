package com.tonyp.dictionarykotlin.common.models

import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipalRelation

data class DictionaryMeaning (
    var id: DictionaryMeaningId = DictionaryMeaningId.NONE,
    var word: String = "",
    var value: String = "",
    var proposedBy: String = "",
    var approved: DictionaryMeaningApproved = DictionaryMeaningApproved.NONE,
    var version: DictionaryMeaningVersion = DictionaryMeaningVersion.NONE,
    var principalRelation: DictionaryPrincipalRelation = DictionaryPrincipalRelation.NONE
) {

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = DictionaryMeaning()
    }
}
