package com.tonyp.dictionarykotlin.repo.inmemory

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId

data class MeaningEntity (
    val id: String? = null,
    val wordEntity: WordEntity? = null,
    val valueEntity: ValueEntity? = null,
    val proposedBy: String? = null,
    val approved: Boolean? = null
) {
    constructor(model: DictionaryMeaning): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        wordEntity = model.word
            .takeIf { it.isNotBlank() }
            ?.let { WordEntity(word = it) },
        valueEntity = model.value
            .takeIf { it.isNotBlank() }
            ?.let { ValueEntity(value = it) },
        proposedBy = model.proposedBy.takeIf { it.isNotBlank() },
        approved = model.approved.boolean
    )

    fun toInternal() = DictionaryMeaning(
        id = id?.let { DictionaryMeaningId(it) }?: DictionaryMeaningId.NONE,
        word = wordEntity?.word ?: "",
        value = valueEntity?.value ?: "",
        proposedBy = proposedBy ?: "",
        approved = DictionaryMeaningApproved.fromBoolean(approved)
    )
}

data class WordEntity (
    val id: String? = null,
    val word: String? = null
)

data class ValueEntity (
    val id: String? = null,
    val value: String? = null
)