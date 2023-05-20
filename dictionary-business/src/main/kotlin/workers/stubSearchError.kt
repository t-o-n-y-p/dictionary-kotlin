package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub

fun CorChainDsl<DictionaryContext>.stubSearchError() = worker {
    this.title = "Имитация неуспешной обработки"
    on {
        stubCase == DictionaryStub.CANNOT_SEARCH
                && state == DictionaryState.RUNNING
    }
    handle {
        fail(DictionaryMeaningStub.getSearchError())
    }
}