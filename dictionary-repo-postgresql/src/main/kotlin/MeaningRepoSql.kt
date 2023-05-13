package com.tonyp.dictionarykotlin.repo.postgresql

import com.benasher44.uuid.uuid4
import com.tonyp.dictionarykotlin.common.helpers.asDictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.repo.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class MeaningRepoSql(
    properties: SqlProperties,
    initObjects: Collection<DictionaryMeaning> = emptyList(),
    val idUuid: () -> String = { uuid4().toString() },
    val versionUuid: () -> String = { uuid4().toString() },
) : IMeaningRepository {

    private fun <T> transactionWrapper(handle: (Exception) -> T, block: () -> T): T =
        try {
            transaction {
                block()
            }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbMeaningResponse): DbMeaningResponse =
        transactionWrapper({ DbMeaningResponse.error(it.asDictionaryError()) }) {
            block()
        }

    init {
        val driver = when {
            properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
        }

        Database.connect(
            properties.url, driver, properties.user, properties.password
        )

        transaction {
            properties.takeIf { it.dropDatabase }?.let { dropTables() }
            createTables()
            initObjects.forEach { createWithNoDuplicateError(it) }
        }
    }

    private fun createWithNoDuplicateError(meaning: DictionaryMeaning) {
        val wordId: String = Words
            .select { Words.word eq meaning.word }
            .singleOrNull()
            ?.let { Words.getId(it) }
            ?: Words.getId(
                Words.insert {
                    to(it, meaning.word, idUuid)
                }
            )
        val valueId: String = Values
            .select { Values.value eq meaning.value }
            .singleOrNull()
            ?.let { Values.getId(it) }
            ?: Values.getId(
                Values.insert {
                    to(it, meaning.value, idUuid)
                }
            )
        Meanings.insert {
            to(
                builder = it,
                wordId = wordId,
                valueId = valueId,
                meaning = meaning,
                idUuid = idUuid,
                versionUuid = versionUuid
            )
        }
    }

    private fun read(id: DictionaryMeaningId): DbMeaningResponse {
        val res = Meanings
            .innerJoin(Words)
            .innerJoin(Values)
            .select {
                Meanings.id eq id.asString()
            }.singleOrNull()
            ?: return IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
        return DbMeaningResponse.success(Meanings.from(res))
    }

    override suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse =
        transactionWrapper {
            val wordId: String = Words
                .select { Words.word eq rq.meaning.word }
                .singleOrNull()
                ?.let { Words.getId(it) }
                ?: Words.getId(
                    Words.insert {
                        to(it, rq.meaning.word, idUuid)
                    }
                )
            val valueId: String = Values
                .select { Values.value eq rq.meaning.value }
                .singleOrNull()
                ?.let { Values.getId(it) }
                ?: Values.getId(
                    Values.insert {
                        to(it, rq.meaning.value, idUuid)
                    }
                )
            val meaningId: String = Meanings
                .select {
                    buildList {
                        add(Meanings.wordId eq wordId)
                        add(Meanings.valueId eq valueId)
                    }.reduce { a, b -> a and b }
                }
                .singleOrNull()
                ?.let { return@transactionWrapper IMeaningRepository.Errors.RESULT_ERROR_ALREADY_EXISTS }
                ?: Meanings.getId(
                    Meanings.insert {
                        to(
                            builder = it,
                            wordId = wordId,
                            valueId = valueId,
                            meaning = rq.meaning,
                            idUuid = idUuid,
                            versionUuid = versionUuid
                        )
                    }
                )
            read(DictionaryMeaningId(meaningId))
        }

    override suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse =
        transactionWrapper {
            rq.id
                .takeIf { it != DictionaryMeaningId.NONE }
                ?.let { read(it) }
                ?: IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
        }

    override suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse =
        transactionWrapper {
            val queryResult: Query = rq.meaning.id
                .takeIf { it != DictionaryMeaningId.NONE }
                ?.let { Meanings.select { Meanings.id eq it.asString() } }
                ?: return@transactionWrapper IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
            val singleRow: ResultRow = queryResult
                .singleOrNull()
                ?: return@transactionWrapper IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
            rq.meaning
                .takeIf { it.version.asString() == singleRow[Meanings.version] }
                ?.let {
                    Meanings.update({ Meanings.id eq it.id.asString() }) {
                        builder -> to(builder, singleRow, it, versionUuid)
                    }
                    read(rq.meaning.id)
                }
                ?: IMeaningRepository.Errors.RESULT_ERROR_CONCURRENT_MODIFICATION
        }

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse =
        transactionWrapper {
            val response: DbMeaningResponse = rq.id
                .takeIf { it != DictionaryMeaningId.NONE }
                ?.let { read(it) }
                ?: IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
            rq.takeIf { it.version == response.data?.version }
                ?.let {
                    Meanings.deleteWhere { id eq rq.id.asString() }
                    response
                }
                ?: IMeaningRepository.Errors.RESULT_ERROR_CONCURRENT_MODIFICATION
        }

    override suspend fun searchMeaning(rq: DbMeaningFilterRequest): DbMeaningsResponse =
        transactionWrapper({ DbMeaningsResponse.error(it.asDictionaryError()) }) {
            val data: List<DictionaryMeaning> = Meanings
                .innerJoin(Words)
                .innerJoin(Values)
                .select {
                    buildList {
                        add(Op.TRUE)
                        rq.filter.word.takeIf { it.isNotBlank() }?.let { add(Words.word eq it) }
                        rq.filter.approved
                            .takeIf { it != DictionaryMeaningApproved.NONE }
                            ?.let { add(Meanings.approved eq it) }
                    }.reduce { a, b -> a and b }
                }.map {
                    Meanings.from(it)
                }
            DbMeaningsResponse.success(data)
        }
}