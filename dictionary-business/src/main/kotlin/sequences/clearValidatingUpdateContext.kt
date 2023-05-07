package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.clearValidatingUpdateContext() = sequence {
    this.title = "Очистка контекста валидации UPDATE"
    on { this.command == DictionaryCommand.UPDATE && state == DictionaryState.RUNNING }
    worker("Очистка id") {
        meaningValidating.id = DictionaryMeaningId(meaningValidating.id.asString().trim())
    }
    worker("Очистка word") { meaningValidating.word = "" }
    worker("Очистка value") { meaningValidating.value = "" }
    worker("Очистка proposedBy") { meaningValidating.proposedBy = "" }
}