package com.tonyp.dictionarykotlin.common.models

data class DictionaryMeaningFilter (

    var word: String = "",
    var approved: DictionaryMeaningApproved = DictionaryMeaningApproved.NONE

)
