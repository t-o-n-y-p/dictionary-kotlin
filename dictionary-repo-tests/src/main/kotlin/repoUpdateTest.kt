package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
import com.tonyp.dictionarykotlin.common.repo.DbMeaningRequest
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun repoUpdateTest(repo: IMeaningRepository) = funSpec {

    repoTest("Update success") {
        val updateObject = InitUpdateObjects.initObjects[0].copy(
            approved = DictionaryMeaningApproved.TRUE
        )
        val result = repo.updateMeaning(DbMeaningRequest(updateObject))

        result.isSuccess shouldBe true
        result.data shouldBe updateObject.copy(version = InitUpdateObjects.initVersion)
        result.errors shouldBe emptyList()
    }

    repoTest("Update error: version empty") {
        val updateObject = InitUpdateObjects.initObjects[1].copy(
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion.NONE
        )
        val result = repo.updateMeaning(DbMeaningRequest(updateObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_VERSION
    }

    repoTest("Update error: concurrent modification") {
        val updateObject = InitUpdateObjects.initObjects[2].copy(
            approved = DictionaryMeaningApproved.TRUE,
            version = InitDeleteObjects.initVersion
        )
        val result = repo.updateMeaning(DbMeaningRequest(updateObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_CONCURRENT_MODIFICATION
    }

    repoTest("Update error: not found") {
        val updateObject = DictionaryMeaning(
            id = DictionaryMeaningId("0"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.TRUE,
            version = InitUpdateObjects.initVersion
        )
        val result = repo.updateMeaning(DbMeaningRequest(updateObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
    }

    repoTest("Update error: empty ID") {
        val updateObject = DictionaryMeaning(
            id = DictionaryMeaningId.NONE,
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.TRUE,
            version = InitUpdateObjects.initVersion
        )
        val result = repo.updateMeaning(DbMeaningRequest(updateObject))

        result shouldBe IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
    }
}

object InitUpdateObjects : InitObjects {
    override val initObjects: List<DictionaryMeaning> =
        DictionaryMeaningStub.getLongSearchData()
}