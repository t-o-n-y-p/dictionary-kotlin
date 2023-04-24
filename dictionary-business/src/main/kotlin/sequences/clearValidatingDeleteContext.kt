package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.clearValidatingDeleteContext() = sequence {
    this.title = "Очистка контекста валидации DELETE"
    on { this.command == DictionaryCommand.DELETE && state == DictionaryState.RUNNING }
    worker("Очистка id") {
        meaningValidating.id = DictionaryMeaningId(meaningValidating.id.asString().trim())
    }
}