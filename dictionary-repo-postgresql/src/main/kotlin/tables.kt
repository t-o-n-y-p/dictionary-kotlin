package com.tonyp.dictionarykotlin.repo.postgresql

import com.benasher44.uuid.uuid4
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object Words : Table("words") {

    val id = varchar("id", 64)
    val word = varchar("word", 32).uniqueIndex()

    override val primaryKey = PrimaryKey(id)

    fun getId(word: String): String = Words
        .select { Words.word eq word }
        .singleOrNull()
        ?.let { it[id].toString() }
        ?: uuid4().toString().also {id ->
            Words.insert {
                it[Words.id] = id
                it[Words.word] = word
            }
        }
}

object Values : Table("values") {

    val id = varchar("id", 64)
    val value = varchar("value", 256).uniqueIndex()

    override val primaryKey = PrimaryKey(id)

    fun getId(value: String): String = Values
        .select { Values.value eq value }
        .singleOrNull()
        ?.let { it[id].toString() }
        ?: uuid4().toString().also {id ->
            Values.insert {
                it[Values.id] = id
                it[Values.value] = value
            }
        }
}

object Meanings : Table("meanings") {

    val id = varchar("id", 64).uniqueIndex()
    val wordId = reference("word_id", Words.id, onDelete = ReferenceOption.CASCADE)
    val valueId = reference("value_id", Values.id, onDelete = ReferenceOption.CASCADE)
    val proposedBy = varchar("proposed_by", 12)
    val approved = enumeration("approved", DictionaryMeaningApproved::class)
    val version = varchar("version", 64)

    override val primaryKey = PrimaryKey(wordId, valueId)

    fun to(
        builder: UpdateBuilder<*>,
        meaning: DictionaryMeaning,
        idUuid: () -> String,
        versionUuid: () -> String
    ) {
        builder[id] = idUuid()
        builder[wordId] = Words.getId(meaning.word)
        builder[valueId] = Values.getId(meaning.value)
        builder[proposedBy] = meaning.proposedBy
        builder[approved] = meaning.approved
        builder[version] = versionUuid()
    }

    fun from(result: ResultRow) = DictionaryMeaning(
        id = DictionaryMeaningId(result[id].toString()),
        word = result[Words.word],
        value = result[Values.value],
        proposedBy = result[proposedBy],
        approved = result[approved],
        version = DictionaryMeaningVersion(result[version].toString())
    )

}

fun dropTables() = drop(Meanings, Words, Values)

fun createTables() = create(Words, Values, Meanings)
