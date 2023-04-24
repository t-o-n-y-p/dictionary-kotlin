package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.clearValidatingReadContext() = sequence {
    this.title = "Очистка контекста валидации READ"
    on { this.command == DictionaryCommand.READ && state == DictionaryState.RUNNING }
    worker("Очистка id") {
        meaningValidating.id = DictionaryMeaningId(meaningValidating.id.asString().trim())
    }
}