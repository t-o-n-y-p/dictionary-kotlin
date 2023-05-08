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
fun repoDeleteTest(repo: IMeaningRepository) = funSpec {

    repoTest("Delete success") {
        val result = repo.deleteMeaning(DbMeaningIdRequest(repoDeleteInitObjects[0]))

        result.isSuccess shouldBe true
        result.data shouldBe repoDeleteInitObjects[0]
        result.errors shouldBe emptyList()
    }

    repoTest("Delete error: not found") {
        val deleteObject = DictionaryMeaning(
            id = DictionaryMeaningId("1"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t-o-n-y-p"
        )
        val result = repo.deleteMeaning(DbMeaningIdRequest(deleteObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
    }

    repoTest("Delete error: empty ID") {
        val deleteObject = DictionaryMeaning(
            id = DictionaryMeaningId.NONE,
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t-o-n-y-p"
        )
        val result = repo.deleteMeaning(DbMeaningIdRequest(deleteObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
    }
}

val repoDeleteInitObjects: List<DictionaryMeaning> = listOf(
    DictionaryMeaningStub.getApproved()
)