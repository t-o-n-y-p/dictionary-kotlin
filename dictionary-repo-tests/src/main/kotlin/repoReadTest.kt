package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.repo.DbMeaningIdRequest
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun repoReadTest(repo: IMeaningRepository) = funSpec {

    repoTest("Read success") {
        val result = repo.readMeaning(DbMeaningIdRequest(repoReadInitObjects[0]))

        result.isSuccess shouldBe true
        result.data shouldBe repoReadInitObjects[0]
        result.errors shouldBe emptyList()
    }

    repoTest("Read error: not found") {
        val readObject = DictionaryMeaning(
            id = DictionaryMeaningId("0"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p"
        )
        val result = repo.readMeaning(DbMeaningIdRequest(readObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
    }

    repoTest("Read error: empty ID") {
        val readObject = DictionaryMeaning(
            id = DictionaryMeaningId.NONE,
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p"
        )
        val result = repo.readMeaning(DbMeaningIdRequest(readObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
    }
}

val repoReadInitObjects: List<DictionaryMeaning> = listOf(
    DictionaryMeaningStub.getApproved()
)