package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateValueContent() = worker {
    this.title = "Проверка, что значение не слишком велико"
    on { meaningValidating.value.length > 256 }
    handle {
        fail(
            errorValidation(
                code = "INVALID_VALUE",
                message = "Value must be 256 symbols at maximum"
            )
        )
    }
}