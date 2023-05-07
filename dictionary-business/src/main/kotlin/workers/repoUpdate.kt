package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.repo.DbMeaningRequest
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoUpdate() = worker {
    this.title = "Изменение объекта"
    on { state == DictionaryState.RUNNING }
    handle {
        val result = meaningRepo.updateMeaning(DbMeaningRequest(meaningRepoPrepare))
        val resultData = result.data
        if (result.isSuccess && resultData != null) {
            meaningRepoDone = resultData
        } else {
            fail(result.errors)
        }
    }
}