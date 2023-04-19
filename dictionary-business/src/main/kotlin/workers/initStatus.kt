package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.initStatus() = worker {
    this.title = "Инициализация статуса"
    on { state == DictionaryState.NONE }
    handle { state = DictionaryState.RUNNING }
}