package com.tonyp.dictionarykotlin.repo.inmemory

import com.benasher44.uuid.uuid4
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
import com.tonyp.dictionarykotlin.common.repo.*
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_ALREADY_EXISTS
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_CONCURRENT_MODIFICATION
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_EMPTY_VERSION
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class MeaningRepoInMemory (
    initObjects: List<DictionaryMeaning> = emptyList(),
    ttl: Duration = 2.minutes,
    val idUuid: () -> String = { uuid4().toString() },
    val versionUuid: () -> String = { uuid4().toString() },
) : IMeaningRepository {

    private val meaningCache = Cache.Builder<String, MeaningEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val wordCache = Cache.Builder<String, WordEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val valueCache = Cache.Builder<String, ValueEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(meaning: DictionaryMeaning) {
        val entity = MeaningEntity(meaning)
        if (entity.id != null) {
            entity.wordEntity
                ?.takeUnless { wordCache.asMap().containsValue(entity.wordEntity) }
                ?.let { wordCache.put(uuid4().toString(), it) }
            entity.valueEntity
                ?.takeUnless { valueCache.asMap().containsValue(entity.valueEntity) }
                ?.let { valueCache.put(uuid4().toString(), it) }
            meaningCache.put(entity.id, entity)
        }
    }

    override suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse {
        meaningCache.asMap().asSequence()
            .firstOrNull {
                it.value.wordEntity?.word == rq.meaning.word
                        && it.value.valueEntity?.value == rq.meaning.value
            }
            ?.let { return RESULT_ERROR_ALREADY_EXISTS }

        val key = idUuid()
        val version = versionUuid()
        val meaning = rq.meaning.copy(
            id = DictionaryMeaningId(key),
            version = DictionaryMeaningVersion(version)
        )
        save(meaning)
        return DbMeaningResponse.success(meaning)
    }

    override suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse {
        val key = rq.id.takeIf { it != DictionaryMeaningId.NONE }?.asString() ?: return RESULT_ERROR_EMPTY_ID
        return meaningCache.get(key)
            ?.let { DbMeaningResponse.success(it.toInternal()) }
            ?: RESULT_ERROR_NOT_FOUND
    }

    override suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse {
        val newMeaning = rq.meaning.copy(
            version = DictionaryMeaningVersion(versionUuid())
        )
        val entity = MeaningEntity(newMeaning)
        return doUpdate(rq.meaning.id, rq.meaning.version) {key, _ ->
            meaningCache.put(key, entity)
            DbMeaningResponse.success(newMeaning)
        }
    }

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse =
        doUpdate(rq.id, rq.version) {key, oldMeaning ->
            meaningCache.invalidate(key)
            DbMeaningResponse.success(oldMeaning.toInternal())
        }

    override suspend fun searchMeaning(rq: DbMeaningFilterRequest): DbMeaningsResponse {
        val result = meaningCache.asMap().asSequence()
            .filter { entry ->
                rq.filter.word.takeIf { it.isNotBlank() }?.let {
                    it == entry.value.wordEntity?.word
                } ?: true
            }
            .filter { entry ->
                rq.filter.approved.takeIf { it != DictionaryMeaningApproved.NONE }?.let {
                    it == DictionaryMeaningApproved.fromBoolean(entry.value.approved)
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbMeaningsResponse.success(result)
    }

    private suspend fun doUpdate(
        id: DictionaryMeaningId,
        version: DictionaryMeaningVersion,
        okBlock: (String, MeaningEntity) -> DbMeaningResponse
    ): DbMeaningResponse = mutex.withLock {
        val key = id
            .takeIf { it != DictionaryMeaningId.NONE }
            ?.asString()
            ?: return RESULT_ERROR_EMPTY_ID
        val versionStr = version
            .takeIf { it != DictionaryMeaningVersion.NONE }
            ?.asString()
            ?: return RESULT_ERROR_EMPTY_VERSION

        val oldMeaning = meaningCache.get(key)
        when (oldMeaning?.version) {
            null -> RESULT_ERROR_NOT_FOUND
            versionStr -> okBlock(key, oldMeaning)
            else -> RESULT_ERROR_CONCURRENT_MODIFICATION
        }
    }
}