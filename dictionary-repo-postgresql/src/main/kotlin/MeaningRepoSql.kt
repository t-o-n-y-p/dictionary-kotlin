package com.tonyp.dictionarykotlin.repo.postgresql

import com.benasher44.uuid.uuid4
import com.tonyp.dictionarykotlin.common.helpers.asDictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
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
            initObjects.forEach {
                Meanings.insert {
                    builder ->
                    to(
                        builder = builder,
                        meaning = it,
                        idUuid = { it.id.asString() },
                        versionUuid = { it.version.asString() }
                    )
                }
            }
        }
    }

    private fun read(id: DictionaryMeaningId): DbMeaningResponse {
        val res: ResultRow = id
            .takeIf { id != DictionaryMeaningId.NONE }
            ?.let {
                Meanings
                    .innerJoin(Words)
                    .innerJoin(Values)
                    .select { Meanings.id eq id.asString() }
                    .singleOrNull()
                    ?: return IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
            }
            ?: return IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
        return DbMeaningResponse.success(Meanings.from(res))
    }

    private fun update(rq: DbMeaningIdRequest, block: (DbMeaningResponse) -> DbMeaningResponse): DbMeaningResponse =
        transactionWrapper {
            rq.version
                .takeIf { it != DictionaryMeaningVersion.NONE }
                ?: return@transactionWrapper IMeaningRepository.Errors.RESULT_ERROR_EMPTY_VERSION
            read(rq.id).takeIf { (it.data?.version ?: return@transactionWrapper it) == rq.version }
                ?.let { block(it) }
                ?: IMeaningRepository.Errors.RESULT_ERROR_CONCURRENT_MODIFICATION
        }

    override suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse =
        transactionWrapper {
            val meaningId: String = Meanings
                .innerJoin(Words)
                .innerJoin(Values)
                .select {
                    buildList {
                        add(Words.word eq rq.meaning.word)
                        add(Values.value eq rq.meaning.value)
                    }.reduce { a, b -> a and b }
                }
                .singleOrNull()
                ?.let { return@transactionWrapper IMeaningRepository.Errors.RESULT_ERROR_ALREADY_EXISTS }
                ?: idUuid().also {
                    Meanings.insert {
                        builder ->
                        to(
                            builder = builder,
                            meaning = rq.meaning,
                            idUuid = { it },
                            versionUuid = versionUuid
                        )
                    }
                }
            read(DictionaryMeaningId(meaningId))
        }

    override suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse =
        transactionWrapper {
             read(rq.id)
        }

    override suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse =
        update(rq.getIdRequest()) {
            Meanings.update({ Meanings.id eq rq.meaning.id.asString() }) {
                builder ->
                to(
                    builder = builder,
                    meaning = rq.meaning,
                    idUuid = { rq.meaning.id.asString() },
                    versionUuid = versionUuid
                )
            }
            read(rq.meaning.id)
        }

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse =
        update(rq) {
            Meanings.deleteWhere { id eq rq.id.asString() }
            it
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