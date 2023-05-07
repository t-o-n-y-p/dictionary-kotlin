package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateIdContent() = worker {
    this.title = "Проверка, что id соответствует шаблону"
    on {
        meaningValidating.id != DictionaryMeaningId.NONE
                && !meaningValidating.id.asString().matches(Regex("\\d+"))
    }
    handle {
        fail(
            errorValidation(
                code = "INVALID_ID",
                message = "ID must be 1 or more digits"
            )
        )
    }
}