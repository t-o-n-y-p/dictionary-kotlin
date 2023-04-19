package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateWordNotEmpty() = worker {
    this.title = "Проверка, что слово не пустое"
    on { meaningValidating.word.isEmpty() }
    handle {
        fail(
            errorValidation(
                code = "WORD_IS_EMPTY",
                message = "Word must not be empty"
            )
        )
    }
}