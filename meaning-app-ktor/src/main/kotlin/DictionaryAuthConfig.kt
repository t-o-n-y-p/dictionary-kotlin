package com.tonyp.dictionarykotlin.meaning.app

import io.ktor.server.application.*
import io.ktor.server.config.*

data class DictionaryAuthConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val clientId: String,
    val certUrl: String? = null,
) {
    constructor(environment: ApplicationEnvironment): this(environment.config)

    constructor(config: ApplicationConfig): this(
        secret = config.propertyOrNull("jwt.secret")?.getString() ?: "",
        issuer = config.property("jwt.issuer").getString(),
        audience = config.property("jwt.audience").getString(),
        realm = config.property("jwt.realm").getString(),
        clientId = config.propertyOrNull("jwt.clientId")?.getString() ?: "",
        certUrl = config.propertyOrNull("jwt.certUrl")?.getString(),
    )

    companion object {
        const val GROUPS_CLAIM = "groups"
        const val NAME_CLAIM = "preferred_username"
    }
}
