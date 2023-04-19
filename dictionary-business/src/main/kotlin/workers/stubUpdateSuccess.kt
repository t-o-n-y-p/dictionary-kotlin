package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub

fun CorChainDsl<DictionaryContext>.stubUpdateSuccess() = worker {
    this.title = "Имитация успешной обработки"
    on {
        command == DictionaryCommand.UPDATE
                && stubCase == DictionaryStub.SUCCESS
                && state == DictionaryState.RUNNING
    }
    handle {
        state = DictionaryState.FINISHING
        meaningResponse = DictionaryMeaningStub.getApproved()
    }
}