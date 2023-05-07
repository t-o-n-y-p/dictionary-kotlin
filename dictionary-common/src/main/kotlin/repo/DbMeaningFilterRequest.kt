package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningFilter

data class DbMeaningFilterRequest (
    val filter: DictionaryMeaningFilter
)
