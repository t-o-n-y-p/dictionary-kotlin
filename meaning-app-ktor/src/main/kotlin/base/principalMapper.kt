package com.tonyp.dictionarykotlin.meaning.app.base

import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig.Companion.GROUPS_CLAIM
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAuthConfig.Companion.NAME_CLAIM
import io.ktor.server.auth.jwt.*

fun JWTPrincipal?.toModel() = this?.run {
    DictionaryPrincipal(
        name = payload.getClaim(NAME_CLAIM).asString(),
        groups = payload
            .getClaim(GROUPS_CLAIM)
            .asList(String::class.java)
            .distinct()
            .mapNotNull { DictionaryUserGroup.valueOf(it) }
            .toSet()
    )
} ?: DictionaryPrincipal.NONE