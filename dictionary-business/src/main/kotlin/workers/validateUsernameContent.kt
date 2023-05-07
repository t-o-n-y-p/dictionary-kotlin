package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateUsernameContent() = worker {
    this.title = "Проверка, что имя пользователя соответствует шаблону"
    on {
        meaningValidating.proposedBy.isNotEmpty()
                && !meaningValidating.proposedBy.matches(Regex("\\w{1,12}"))
    }
    handle {
        fail(
            errorValidation(
                code = "INVALID_USERNAME",
                message = "Username must be 1-12 latin letters or underscores total"
            )
        )
    }
}