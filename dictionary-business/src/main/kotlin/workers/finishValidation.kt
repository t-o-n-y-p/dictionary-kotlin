package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.finishMeaningValidation() = worker {
    this.title = "Успешное завершение валидации объекта"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningValidated = meaningValidating
    }
}

fun CorChainDsl<DictionaryContext>.finishFilterValidation() = worker {
    this.title = "Успешное завершение валидации фильтра"
    on { state == DictionaryState.RUNNING }
    handle {
        meaningFilterValidated = meaningFilterValidating
    }
}