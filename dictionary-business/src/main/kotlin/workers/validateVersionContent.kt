package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateVersionContent() = worker {
    this.title = "Проверка, что version соответствует шаблону"
    on {
        meaningValidating.version != DictionaryMeaningVersion.NONE
                && !meaningValidating.version.asString().matches(Regex("[0-9a-zA-Z-]{1,64}"))
    }
    handle {
        fail(
            errorValidation(
                code = "INVALID_VERSION",
                message = "Version must be mo more than 64 digits, latin chars, or dashes"
            )
        )
    }
}