package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence

fun CorChainDsl<DictionaryContext>.stubs(block: CorChainDsl<DictionaryContext>.() -> Unit) = sequence {
    this.title = "Обработка стабов"
    on { workMode == DictionaryWorkMode.STUB && state == DictionaryState.RUNNING }
    block()
}