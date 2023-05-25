package com.tonyp.dictionarykotlin.repo.inmemory

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion

data class MeaningEntity (
    val id: String? = null,
    val wordEntity: WordEntity? = null,
    val valueEntity: ValueEntity? = null,
    val userEntity: UserEntity? = null,
    val approved: Boolean? = null,
    val version: String? = null
) {
    constructor(model: DictionaryMeaning): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        wordEntity = model.word
            .takeIf { it.isNotBlank() }
            ?.let { WordEntity(word = it) },
        valueEntity = model.value
            .takeIf { it.isNotBlank() }
            ?.let { ValueEntity(value = it) },
        userEntity = model.proposedBy
            .takeIf { it.isNotBlank() }
            ?.let { UserEntity(name = it) },
        approved = model.approved.boolean,
        version = model.version.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = DictionaryMeaning(
        id = id?.let { DictionaryMeaningId(it) }?: DictionaryMeaningId.NONE,
        word = wordEntity?.word ?: "",
        value = valueEntity?.value ?: "",
        proposedBy = userEntity?.name ?: "",
        approved = DictionaryMeaningApproved.fromBoolean(approved),
        version = version?.let { DictionaryMeaningVersion(it) } ?: DictionaryMeaningVersion.NONE
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

data class UserEntity (
    val id: String? = null,
    val name: String? = null
)