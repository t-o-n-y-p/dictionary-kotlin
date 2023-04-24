package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.clearValidatingSearchContext() = sequence {
    this.title = "Очистка контекста валидации SEARCH"
    on { this.command == DictionaryCommand.SEARCH && state == DictionaryState.RUNNING }
    worker("Очистка word") {
        meaningFilterValidating.word = meaningFilterValidating.word.trim()
    }
}