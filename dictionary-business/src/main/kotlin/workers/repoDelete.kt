package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.repo.DbMeaningIdRequest
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoDelete() = worker {
    this.title = "Удаление объекта по ID"
    on { state == DictionaryState.RUNNING }
    handle {
        val result = meaningRepo.deleteMeaning(DbMeaningIdRequest(meaningRepoPrepare))
        if (result.isSuccess) {
            meaningRepoDone = meaningRepoRead
        } else {
            fail(result.errors)
        }
    }
}