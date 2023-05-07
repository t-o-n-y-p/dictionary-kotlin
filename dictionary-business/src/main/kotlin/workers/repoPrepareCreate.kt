package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoPrepareCreate() = worker {
    this.title = "Подготовка объекта для создания"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningRepoPrepare = meaningValidated.copy()
    }
}