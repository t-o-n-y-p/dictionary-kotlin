package com.tonyp.dictionarykotlin.common.repo

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning

data class DbMeaningRequest (
    val meaning: DictionaryMeaning
) {
    fun getIdRequest() = DbMeaningIdRequest(id = meaning.id, version = meaning.version)
}
