package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningFilter
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.repo.DbMeaningFilterRequest
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun repoSearchTest(repo: IMeaningRepository) = funSpec {

    repoTest("Search with empty filter") {
        val result = repo.searchMeaning(DbMeaningFilterRequest(DictionaryMeaningFilter()))

        result.isSuccess shouldBe true
        result.data shouldBe repoSearchInitObjects
        result.errors shouldBe emptyList()
    }

    repoTest("Search by word") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(DictionaryMeaningFilter(word = "трава"))
        )

        result.isSuccess shouldBe true
        result.data shouldBe repoSearchInitObjects.filter { it.word == "трава" }
        result.errors shouldBe emptyList()
    }

    repoTest("Search by approved flag") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(DictionaryMeaningFilter(approved = DictionaryMeaningApproved.TRUE))
        )

        result.isSuccess shouldBe true
        result.data shouldBe repoSearchInitObjects.filter { it.approved == DictionaryMeaningApproved.TRUE }
        result.errors shouldBe emptyList()
    }

    repoTest("Search by word and approved flag") {
        val result = repo.searchMeaning(
            DbMeaningFilterRequest(DictionaryMeaningFilter(word = "обвал", approved = DictionaryMeaningApproved.FALSE))
        )

        result.isSuccess shouldBe true
        result.data shouldBe repoSearchInitObjects.filter {
            it.word == "обвал" && it.approved == DictionaryMeaningApproved.FALSE
        }
        result.errors shouldBe emptyList()
    }
}


val repoSearchInitObjects: List<DictionaryMeaning> = listOf(
    DictionaryMeaning(
        id = DictionaryMeaningId("123"),
        word = "трава",
        value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
        proposedBy = "unittest",
        approved = DictionaryMeaningApproved.TRUE
    ),
    DictionaryMeaning(
        id = DictionaryMeaningId("456"),
        word = "обвал",
        value = "снежные глыбы или обломки скал, обрушившиеся с гор",
        proposedBy = "t-o-n-y-p",
        approved = DictionaryMeaningApproved.FALSE
    ),
    DictionaryMeaning(
        id = DictionaryMeaningId("789"),
        word = "трава",
        value = "снежные глыбы или обломки скал, обрушившиеся с гор",
        proposedBy = "unittest",
        approved = DictionaryMeaningApproved.FALSE
    ),
    DictionaryMeaning(
        id = DictionaryMeaningId("34567"),
        word = "обвал",
        value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
        proposedBy = "t-o-n-y-p",
        approved = DictionaryMeaningApproved.TRUE
    )
)