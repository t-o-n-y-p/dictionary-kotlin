package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.auth.checkPermitted
import com.tonyp.dictionarykotlin.auth.resolveRelationTo
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.accessValidation() = sequence {
    this.title = "Вычисление прав доступа по таблице"
    on { state == DictionaryState.RUNNING }
    worker("Вычисление отношения к принципалу") {
        meaningRepoPrepare.principalRelation = meaningRepoPrepare.resolveRelationTo(principal)
    }
    worker("Вычисление доступа") {
        permitted = checkPermitted(command, meaningRepoPrepare.principalRelation, permissions)
    }
    worker {
        this.title = "Валидация прав доступа"
        on { !permitted }
        handle {
            fail(DictionaryError(
                code = "ACCESS_DENIED",
                message = "User is not allowed to perform this operation"
            ))
        }
    }
}