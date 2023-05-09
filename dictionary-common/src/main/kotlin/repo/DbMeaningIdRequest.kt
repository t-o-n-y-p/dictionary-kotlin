package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion

data class DbMeaningIdRequest (
    val id: DictionaryMeaningId,
    val version: DictionaryMeaningVersion = DictionaryMeaningVersion.NONE
) {
    constructor(meaning: DictionaryMeaning): this(meaning.id, meaning.version)
}
