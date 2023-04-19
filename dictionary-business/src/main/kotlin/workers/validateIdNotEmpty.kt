package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateIdNotEmpty() = worker {
    this.title = "Проверка, что id не пустое"
    on { meaningValidating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                code = "ID_IS_EMPTY",
                message = "ID must not be empty"
            )
        )
    }
}