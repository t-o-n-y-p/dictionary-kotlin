package com.tonyp.dictionarykotlin.repo.tests

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningVersion
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

interface InitObjects {
    val initObjects: List<DictionaryMeaning>
    val initVersion: DictionaryMeaningVersion
        get() = DictionaryMeaningVersion("20000000-0000-0000-0000-000000000001")
    val initId: DictionaryMeaningId
        get() = DictionaryMeaningId("1234567890")
}