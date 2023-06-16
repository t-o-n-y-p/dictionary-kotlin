package com.tonyp.dictionarykotlin.business.workers

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.worker
import com.tonyp.dictionarykotlin.auth.resolvePermissions

fun CorChainDsl<DictionaryContext>.resolvePermissions() = worker {
    this.title = "Вычисление прав доступа для групп пользователей"
    on { state == DictionaryState.RUNNING }
    handle {
        permissions.addAll(resolvePermissions(principal.groups))
    }
}