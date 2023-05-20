package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.prepareResult() = worker {
    this.title = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != DictionaryWorkMode.STUB && state == DictionaryState.RUNNING }
    handle {
        meaningResponse = meaningRepoDone
        meaningsResponse = meaningsRepoDone
        state = DictionaryState.FINISHING
    }
}