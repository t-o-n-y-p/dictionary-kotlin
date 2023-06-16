package com.tonyp.dictionarykotlin.auth

import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipalRelation
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserPermission

fun checkPermitted(
    command: DictionaryCommand,
    relation: DictionaryPrincipalRelation,
    permissions: Iterable<DictionaryUserPermission>,
) =
    permissions
        .map {
            AccessTableConditions(
                command = command,
                permission = it,
                relation = relation,
            )
        }.any {
            accessTable[it] ?: false
        }

private data class AccessTableConditions(
    val command: DictionaryCommand,
    val permission: DictionaryUserPermission,
    val relation: DictionaryPrincipalRelation
)

private val accessTable = mapOf(
    AccessTableConditions(
        command = DictionaryCommand.CREATE,
        permission = DictionaryUserPermission.CREATE_OWN,
        relation = DictionaryPrincipalRelation.OWN
    ) to true,
    AccessTableConditions(
        command = DictionaryCommand.CREATE,
        permission = DictionaryUserPermission.CREATE_ALL,
        relation = DictionaryPrincipalRelation.OWN
    ) to true,
    AccessTableConditions(
        command = DictionaryCommand.CREATE,
        permission = DictionaryUserPermission.CREATE_ALL,
        relation = DictionaryPrincipalRelation.NONE
    ) to true,
    AccessTableConditions(
        command = DictionaryCommand.UPDATE,
        permission = DictionaryUserPermission.MANAGE,
        relation = DictionaryPrincipalRelation.OWN
    ) to true,
    AccessTableConditions(
        command = DictionaryCommand.UPDATE,
        permission = DictionaryUserPermission.MANAGE,
        relation = DictionaryPrincipalRelation.NONE
    ) to true,
    AccessTableConditions(
        command = DictionaryCommand.DELETE,
        permission = DictionaryUserPermission.MANAGE,
        relation = DictionaryPrincipalRelation.OWN
    ) to true,
    AccessTableConditions(
        command = DictionaryCommand.DELETE,
        permission = DictionaryUserPermission.MANAGE,
        relation = DictionaryPrincipalRelation.NONE
    ) to true
)