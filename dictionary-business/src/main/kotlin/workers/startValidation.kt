package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.startMeaningValidation() = worker {
    this.title = "Начало валидации объекта"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningValidating = meaningRequest.copy()
    }
}

fun CorChainDsl<DictionaryContext>.startFilterValidation() = worker {
    this.title = "Начало валидации фильтра"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningFilterValidating = meaningFilterRequest.copy()
    }
}