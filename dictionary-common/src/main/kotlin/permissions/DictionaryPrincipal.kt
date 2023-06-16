package com.tonyp.dictionarykotlin.common.permissions

data class DictionaryPrincipal(
    val name: String = "",
    val groups: Set<DictionaryUserGroup> = emptySet()
) {
    companion object {
        val NONE = DictionaryPrincipal()
    }
}
