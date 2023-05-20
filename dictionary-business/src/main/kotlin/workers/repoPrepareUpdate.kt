package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.repoPrepareUpdate() = worker {
    this.title = "Подготовка объекта для изменения"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningRepoPrepare = meaningRepoRead.copy().apply {
            approved = meaningValidated.approved
            version = meaningValidated.version
        }
    }
}