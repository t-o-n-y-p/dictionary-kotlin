package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId

data class DbMeaningIdRequest (
    val id: DictionaryMeaningId
) {
    constructor(meaning: DictionaryMeaning): this(meaning.id)
}
