package com.tonyp.dictionarykotlin.auth

import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserPermission

fun resolvePermissions(
    groups: Iterable<DictionaryUserGroup>
) = mutableSetOf<DictionaryUserPermission>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    DictionaryUserGroup.USER to setOf(
        DictionaryUserPermission.CREATE_OWN
    ),
    DictionaryUserGroup.ADMIN to setOf(
        DictionaryUserPermission.CREATE_ALL,
        DictionaryUserPermission.MANAGE
    ),
    DictionaryUserGroup.BANNED to emptySet(),
    DictionaryUserGroup.TEST to emptySet()
)

private val groupPermissionsDenys = mapOf(
    DictionaryUserGroup.USER to emptySet(),
    DictionaryUserGroup.ADMIN to emptySet(),
    DictionaryUserGroup.BANNED to setOf(
        DictionaryUserPermission.CREATE_OWN,
        DictionaryUserPermission.CREATE_ALL,
        DictionaryUserPermission.MANAGE
    ),
    DictionaryUserGroup.TEST to emptySet()
)
