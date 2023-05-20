package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoPrepareReadResult() = worker {
    this.title = "Завершение операции чтения объекта"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningRepoDone = meaningRepoRead
    }
}