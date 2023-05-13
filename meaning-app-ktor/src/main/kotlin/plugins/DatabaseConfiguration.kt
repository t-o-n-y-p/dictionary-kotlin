package com.tonyp.dictionarykotlin.meaning.app.plugins

import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.meaning.app.configs.PostgresConfig
import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.postgresql.MeaningRepoSql
import com.tonyp.dictionarykotlin.repo.postgresql.SqlProperties
import io.ktor.server.application.*

fun Application.getRepository(mode: DictionaryWorkMode): IMeaningRepository =
    mode.takeIf { it != DictionaryWorkMode.STUB }?.let {
        val dbSettingPath = "dictionary.repository.${it.name.lowercase()}"
        when (environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()) {
            "in-memory", "inmemory", "memory", "mem" -> MeaningRepoInMemory()
            "postgres", "postgresql", "pg", "sql", "psql" -> getPostgresRepo()
            else -> throw IllegalArgumentException(
                "$dbSettingPath must be set in application.yml to one of: 'in-memory', 'psql'"
            )
        }
    } ?: IMeaningRepository.NONE

private fun Application.getPostgresRepo(): IMeaningRepository {
    val config = PostgresConfig(environment.config)
    return MeaningRepoSql(
        properties = SqlProperties(
            url = config.url,
            user = config.user,
            password = config.password,
            schema = config.schema,
        )
    )
}