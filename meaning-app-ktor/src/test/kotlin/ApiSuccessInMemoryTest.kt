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
            initObjects = InitCreateObjects.initObjects,
            idUuid = { createResponseTestSuccess.meaning!!.id!! },
            versionUuid = { createResponseTestSuccess.meaning!!.version!! }
        )

        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(createRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe createResponseTestSuccess
        }
    }

    test("Read request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitReadObjects.initObjects
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
            initObjects = InitUpdateObjects.initObjects,
            versionUuid = { "db6d3220-cb83-46ba-b074-40e49f2a8c65" }
        )
        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(updateRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponseTestSuccess
        }
    }

    test("Delete request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitDeleteObjects.initObjects
        )
        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(deleteRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponseTestSuccess
        }
    }

    test("Search request success in memory") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitSearchObjects.initObjects
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