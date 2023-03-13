package com.tonyp.dictionarykotlin.common.models

data class DictionaryMeaningFilter (

    val word: String = "",
    val approved: DictionaryMeaningApproved = DictionaryMeaningApproved.NONE

)
