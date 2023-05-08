import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import util.DataProvider.createRequestTestSuccess
import util.DataProvider.createResponseTestSuccess
import util.DataProvider.deleteRequestTestSuccess
import util.DataProvider.deleteResponseTestSuccess
import util.DataProvider.readRequestTestSuccess
import util.DataProvider.readResponseTestSuccess
import util.DataProvider.searchRequestTestSuccess
import util.DataProvider.searchResponseTestSuccess
import util.DataProvider.updateRequestTestSuccess
import util.DataProvider.updateResponseTestSuccess
import util.post
import util.testApplication

class ApiSuccessInMemoryTest : FunSpec ({

    test("Create request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = repoCreateInitObjects,
            randomUuid = { createResponseTestSuccess.meaning!!.id!! })

        testApplication(meaningRepoInMemory) {
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                setBody(createRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe createResponseTestSuccess
        }
    }

    test("Read request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = repoReadInitObjects
        )
        testApplication(meaningRepoInMemory) {
            val response = post("/api/v1/meaning/read") {
                contentType(ContentType.Application.Json)
                setBody(readRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningReadResponse shouldBe readResponseTestSuccess
        }
    }

    test("Update request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = repoUpdateInitObjects
        )
        testApplication(meaningRepoInMemory) {
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                setBody(updateRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponseTestSuccess
        }
    }

    test("Delete request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = repoDeleteInitObjects
        )
        testApplication(meaningRepoInMemory) {
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                setBody(deleteRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponseTestSuccess
        }
    }

    test("Search request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = repoSearchInitObjects
        )
        testApplication(meaningRepoInMemory) {
            val response = post("/api/v1/meaning/search") {
                contentType(ContentType.Application.Json)
                setBody(searchRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningSearchResponse shouldBe searchResponseTestSuccess
        }
    }

})