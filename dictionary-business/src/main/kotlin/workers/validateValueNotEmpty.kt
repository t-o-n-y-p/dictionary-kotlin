package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateValueNotEmpty() = worker {
    this.title = "Проверка, что значение не пустое"
    on { meaningValidating.value.isEmpty() }
    handle {
        fail(
            errorValidation(
                code = "VALUE_IS_EMPTY",
                message = "Value must not be empty"
            )
        )
    }
}