package com.tonyp.dictionarykotlin.common.models

data class DictionaryMeaningFilter (

    var word: String = "",
    var mode: DictionaryMeaningFilterMode = DictionaryMeaningFilterMode.NONE,
    var approved: DictionaryMeaningApproved = DictionaryMeaningApproved.NONE

)
