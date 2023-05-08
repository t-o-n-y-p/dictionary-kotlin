package com.tonyp.dictionarykotlin.repo.tests

import io.kotest.core.spec.style.FunSpecTestFactoryConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
fun FunSpecTestFactoryConfiguration.repoTest(
    description: String,
    block: suspend TestScope.() -> Unit
) = test(description) {
    runTest {
        withContext(Dispatchers.Default) {
            block()
        }
    }
}