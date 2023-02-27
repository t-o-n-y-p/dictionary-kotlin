package com.tonyp.dictionarykotlin.common.models

data class DictionaryMeaning (

    var id: DictionaryMeaningId = DictionaryMeaningId.NONE,
    var word: String = "",
    var meaning: String = "",
    var proposedBy: String = "",
    var approved: DictionaryMeaningApproved = DictionaryMeaningApproved.NONE
)
