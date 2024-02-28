package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningFilter
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningFilterMode
import com.tonyp.dictionarykotlin.common.repo.DbMeaningFilterRequest
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun repoSearchTest(repo: IMeaningRepository) = funSpec {

    repoTest("Search with empty filter") {
        val result = repo.searchMeaning(DbMeaningFilterRequest(DictionaryMeaningFilter()))

        result.isSuccess shouldBe true
        result.data shouldContainExactlyInAnyOrder InitSearchObjects.initObjects
        result.errors shouldBe emptyList()
    }

    repoTest("Search by exact word") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(DictionaryMeaningFilter(word = "обвал"))
        )

        result.isSuccess shouldBe true
        result.data shouldContainExactlyInAnyOrder
                InitSearchObjects.initObjects.filter { it.word == "обвал" }
        result.errors shouldBe emptyList()
    }

    repoTest("Search by start of the word") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(
                DictionaryMeaningFilter(
                    word = "обвал",
                    mode = DictionaryMeaningFilterMode.STARTS_WITH))
        )

        result.isSuccess shouldBe true
        result.data shouldContainExactlyInAnyOrder
                InitSearchObjects.initObjects.filter { it.word.startsWith("обвал") }
        result.errors shouldBe emptyList()
    }

    repoTest("Search by any part of the word") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(
                DictionaryMeaningFilter(
                    word = "обвал",
                    mode = DictionaryMeaningFilterMode.CONTAINS))
        )

        result.isSuccess shouldBe true
        result.data shouldContainExactlyInAnyOrder
                InitSearchObjects.initObjects.filter { it.word.contains("обвал") }
        result.errors shouldBe emptyList()
    }

    repoTest("Search by approved flag") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(DictionaryMeaningFilter(approved = DictionaryMeaningApproved.TRUE))
        )

        result.isSuccess shouldBe true
        result.data shouldContainExactlyInAnyOrder
                InitSearchObjects.initObjects.filter { it.approved == DictionaryMeaningApproved.TRUE }
        result.errors shouldBe emptyList()
    }

    repoTest("Search by word and approved flag") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(DictionaryMeaningFilter(word = "трава", approved = DictionaryMeaningApproved.FALSE))
        )

        result.isSuccess shouldBe true
        result.data shouldContainExactlyInAnyOrder
                InitSearchObjects.initObjects.filter {
                    it.word == "трава" && it.approved == DictionaryMeaningApproved.FALSE
                }
        result.errors shouldBe emptyList()
    }
}

object InitSearchObjects : InitObjects {
    override val initObjects: List<DictionaryMeaning> =
        DictionaryMeaningStub.getLongSearchData()
}