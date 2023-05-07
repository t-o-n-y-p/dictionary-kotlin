package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence

fun CorChainDsl<DictionaryContext>.validation(block: CorChainDsl<DictionaryContext>.() -> Unit) = sequence {
    title = "Валидация"
    on { state == DictionaryState.RUNNING }
    block()
}