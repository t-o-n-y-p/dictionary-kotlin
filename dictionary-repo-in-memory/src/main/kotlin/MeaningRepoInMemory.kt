package com.tonyp.dictionarykotlin.repo.inmemory

import com.benasher44.uuid.uuid4
import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.repo.*
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class MeaningRepoInMemory (
    initObjects: List<DictionaryMeaning> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : IMeaningRepository {

    private val cache = Cache.Builder<String, MeaningEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    private fun save(meaning: DictionaryMeaning) {
        val entity = MeaningEntity(meaning)
        if (entity.id != null) {
            cache.put(entity.id, entity)
        }
    }

    override suspend fun createMeaning(rq: DbMeaningRequest): DbMeaningResponse {
        val key = randomUuid()
        val meaning = rq.meaning.copy(id = DictionaryMeaningId(key))
        val entity = MeaningEntity(meaning)
        cache.put(key, entity)
        return DbMeaningResponse(
            data = meaning,
            isSuccess = true
        )
    }

    override suspend fun readMeaning(rq: DbMeaningIdRequest): DbMeaningResponse {
        val key = rq.id.takeIf { it != DictionaryMeaningId.NONE }?.asString() ?: return RESULT_ERROR_EMPTY_ID
        return cache.get(key)
            ?.let {
                DbMeaningResponse(
                    data = it.toInternal(),
                    isSuccess = true,
                )
            } ?: RESULT_ERROR_NOT_FOUND
    }

    override suspend fun updateMeaning(rq: DbMeaningRequest): DbMeaningResponse {
        val key = rq.meaning.id.takeIf { it != DictionaryMeaningId.NONE }?.asString() ?: return RESULT_ERROR_EMPTY_ID
        val newMeaning = rq.meaning.copy()
        val entity = MeaningEntity(newMeaning)
        return when (cache.get(key)) {
            null -> RESULT_ERROR_NOT_FOUND
            else -> {
                cache.put(key, entity)
                DbMeaningResponse(
                    data = newMeaning,
                    isSuccess = true
                )
            }
        }
    }

    override suspend fun deleteMeaning(rq: DbMeaningIdRequest): DbMeaningResponse {
        val key = rq.id.takeIf { it != DictionaryMeaningId.NONE }?.asString() ?: return RESULT_ERROR_EMPTY_ID
        return when (val oldMeaning = cache.get(key)) {
            null -> RESULT_ERROR_NOT_FOUND
            else -> {
                cache.invalidate(key)
                DbMeaningResponse(
                    data = oldMeaning.toInternal(),
                    isSuccess = true
                )
            }
        }
    }

    override suspend fun searchMeaning(rq: DbMeaningFilterRequest): DbMeaningsResponse {
        val result = cache.asMap().asSequence()
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
        return DbMeaningsResponse(
            data = result,
            isSuccess = true
        )
    }

    companion object {
        val RESULT_ERROR_EMPTY_ID = DbMeaningResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                DictionaryError(
                    code = "ID_IS_EMPTY",
                    message = "ID must not be empty"
                )
            )
        )
        val RESULT_ERROR_NOT_FOUND = DbMeaningResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                DictionaryError(
                    code = "NOT_FOUND",
                    message = "Meaning with the provided ID doesn't exist"
                )
            )
        )
    }
}