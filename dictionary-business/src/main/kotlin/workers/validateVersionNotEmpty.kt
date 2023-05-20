package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateVersionNotEmpty() = worker {
    this.title = "Проверка, что version не пустое"
    on { meaningValidating.version.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                code = "VERSION_IS_EMPTY",
                message = "Version must not be empty"
            )
        )
    }
}