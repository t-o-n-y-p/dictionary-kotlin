package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.repo.DbMeaningFilterRequest
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoSearch() = worker {
    this.title = "Поиск объекта в БД по фильтру"
    on { state == DictionaryState.RUNNING }
    handle {
        val result = meaningRepo.searchMeaning(DbMeaningFilterRequest(meaningFilterValidated))
        val resultData = result.data
        if (result.isSuccess && resultData != null) {
            meaningsRepoDone = resultData.toMutableList()
        } else {
            fail(result.errors)
        }
    }
}