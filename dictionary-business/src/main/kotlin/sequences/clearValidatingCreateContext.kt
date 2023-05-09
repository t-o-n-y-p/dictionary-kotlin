package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.clearValidatingCreateContext() = sequence {
    this.title = "Очистка контекста валидации CREATE"
    on { this.command == DictionaryCommand.CREATE && state == DictionaryState.RUNNING }
    worker("Очистка id") { meaningValidating.id = DictionaryMeaningId.NONE }
    worker("Очистка word") { meaningValidating.word = meaningValidating.word.trim() }
    worker("Очистка value") { meaningValidating.value = meaningValidating.value.trim() }
    worker("Очистка proposedBy") { meaningValidating.proposedBy = meaningValidating.proposedBy.trim() }
    worker("Очистка approved") { meaningValidating.approved = DictionaryMeaningApproved.NONE }
    worker("Очистка version") { meaningValidating.version = DictionaryMeaningVersion.NONE }
}