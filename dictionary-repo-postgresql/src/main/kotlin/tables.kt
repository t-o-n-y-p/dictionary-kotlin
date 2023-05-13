package com.tonyp.dictionarykotlin.repo.postgresql

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object Words : Table("words") {

    val id = varchar("id", 64)
    val word = varchar("word", 32).uniqueIndex()

    override val primaryKey = PrimaryKey(id)

    fun to(builder: UpdateBuilder<*>, word: String, idUuid: () -> String) {
        builder[id] = idUuid()
        builder[this.word] = word
    }

    fun getId(result: ResultRow): String = result[id].toString()

    fun getId(result: InsertStatement<*>): String = result[id].toString()
}

object Values : Table("values") {

    val id = varchar("id", 64)
    val value = varchar("word", 256).uniqueIndex()

    override val primaryKey = PrimaryKey(id)

    fun to(builder: UpdateBuilder<*>, value: String, idUuid: () -> String) {
        builder[id] = idUuid()
        builder[this.value] = value
    }

    fun getId(result: ResultRow): String = result[id].toString()

    fun getId(result: InsertStatement<*>): String = result[id].toString()
}

object Meanings : Table("meanings") {

    val id = varchar("id", 64).uniqueIndex()
    val wordId = reference("word_id", Words.id, onDelete = ReferenceOption.CASCADE)
    val valueId = reference("value_id", Values.id, onDelete = ReferenceOption.CASCADE)
    val proposedBy = varchar("proposed_by", 12)
    val approved = enumeration("approved", DictionaryMeaningApproved::class)
    var version = varchar("version", 64)

    override val primaryKey = PrimaryKey(wordId, valueId)

    fun to(
        builder: UpdateBuilder<*>,
        wordId: String,
        valueId: String,
        meaning: DictionaryMeaning,
        idUuid: () -> String,
        versionUuid: () -> String
    ) {
        builder[id] = idUuid()
        builder[this.wordId] = wordId
        builder[this.valueId] = valueId
        builder[proposedBy] = meaning.proposedBy
        builder[approved] = meaning.approved
        builder[version] = versionUuid()
    }

    fun to(
        builder: UpdateBuilder<*>,
        row: ResultRow,
        meaning: DictionaryMeaning,
        versionUuid: () -> String
    ) {
        builder[id] = row[id]
        builder[wordId] = row[wordId]
        builder[valueId] = row[valueId]
        builder[proposedBy] = row[proposedBy]
        builder[approved] = meaning.approved
        builder[version] = versionUuid()
    }

    fun getId(result: InsertStatement<*>): String = result[id].toString()

    fun getId(result: ResultRow): String = result[id].toString()

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
