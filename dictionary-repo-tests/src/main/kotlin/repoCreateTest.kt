package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.repo.DbMeaningRequest
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_ALREADY_EXISTS
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun repoCreateTest(repo: IMeaningRepository) = funSpec {

    repoTest("Create success") {
        val createObject = DictionaryMeaning(
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE
        )
        val result = repo.createMeaning(DbMeaningRequest(createObject))
        val expected = createObject.copy(
            id = InitCreateObjects.initId,
            version = InitCreateObjects.initVersion
        )

        result.isSuccess shouldBe true
        result.data shouldBe expected
        result.errors shouldBe emptyList()
    }

    repoTest("Create error: already exists") {
        val createObject = DictionaryMeaning(
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p"
        )
        val result = repo.createMeaning(DbMeaningRequest(createObject))

        result shouldBe RESULT_ERROR_ALREADY_EXISTS
    }
}

object InitCreateObjects : InitObjects {
    override val initObjects: List<DictionaryMeaning> =
        listOf(DictionaryMeaningStub.getPending())
}