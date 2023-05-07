package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorValidation
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.validateApprovedNotEmpty() = worker {
    this.title = "Проверка, что approved не пустое"
    on { meaningValidating.approved == DictionaryMeaningApproved.NONE }
    handle {
        fail(
            errorValidation(
                code = "APPROVED_FLAG_IS_EMPTY",
                message = "Approved flag must not be empty"
            )
        )
    }
}