package com.tonyp.dictionarykotlin.meaning.app.configs

import io.ktor.server.config.*

data class PostgresConfig(
    val url: String = "jdbc:postgresql://localhost:5432/dictionary",
    val user: String = "postgres",
    val password: String = "dictionary",
    val schema: String = "dictionary",
) {
    constructor(config: ApplicationConfig): this(
        url = config.property("$PATH.url").getString(),
        user = config.property("$PATH.user").getString(),
        password = config.property("$PATH.password").getString(),
        schema = config.property("$PATH.schema").getString(),
    )

    companion object {
        const val PATH = "dictionary.repository.psql"
    }
}