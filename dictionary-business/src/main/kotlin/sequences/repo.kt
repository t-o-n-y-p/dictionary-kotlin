package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence

fun CorChainDsl<DictionaryContext>.repo(
    title: String,
    block: CorChainDsl<DictionaryContext>.() -> Unit
) = sequence {
    this.title = title
    on { state == DictionaryState.RUNNING }
    block()
}