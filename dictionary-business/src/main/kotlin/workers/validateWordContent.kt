package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateWordContent() = worker {
    this.title = "Проверка, что слово состоит из русских букв"
    on {
        meaningValidating.word.isNotEmpty()
                && !meaningValidating.word.matches(Regex("[А-Яа-яЁё]{1,32}"))
    }
    handle {
        fail(
            errorValidation(
                code = "INVALID_WORD",
                message = "Word must be 1-32 russian letters"
            )
        )
    }
}