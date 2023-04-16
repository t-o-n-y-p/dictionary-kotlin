package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence

fun CorChainDsl<DictionaryContext>.operation(
    title: String,
    command: DictionaryCommand,
    block: CorChainDsl<DictionaryContext>.() -> Unit
) = sequence {
    this.title = title
    on { this.command == command && state == DictionaryState.RUNNING }
    block()
}