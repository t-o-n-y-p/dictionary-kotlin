package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.repo.DbMeaningIdRequest
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoRead() = worker {
    this.title = "Чтение объекта из БД"
    on { state == DictionaryState.RUNNING }
    handle {
        val result = meaningRepo.readMeaning(DbMeaningIdRequest(meaningValidated))
        val resultData = result.data
        if (result.isSuccess && resultData != null) {
            meaningRepoRead = resultData
        } else {
            fail(result.errors)
        }
    }
}