package com.tonyp.dictionarykotlin.repo.postgresql

open class SqlProperties(
    val url: String = "jdbc:postgresql://localhost:5432/dictionary",
    val user: String = "postgres",
    val password: String = "dictionary",
    val schema: String = "dictionary",
    // Удалять таблицы при старте - нужно для тестирования
    val dropDatabase: Boolean = false,
)