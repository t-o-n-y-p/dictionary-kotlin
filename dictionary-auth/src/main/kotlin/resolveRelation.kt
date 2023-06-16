package com.tonyp.dictionarykotlin.auth

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipalRelation

fun DictionaryMeaning.resolveRelationTo(principal: DictionaryPrincipal): DictionaryPrincipalRelation =
    proposedBy
        .takeIf { it == principal.name }
        ?.let { DictionaryPrincipalRelation.OWN }
        ?: DictionaryPrincipalRelation.NONE