import com.tonyp.dictionarykotlin.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningReadResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningUpdateResponse
import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import util.DataProvider.createRequestTestError
import util.DataProvider.createResponseTestError
import util.DataProvider.deleteRequestTestConcurrentModification
import util.DataProvider.deleteRequestTestError
import util.DataProvider.deleteResponseTestConcurrentModification
import util.DataProvider.deleteResponseTestError
import util.DataProvider.readRequestTestError
import util.DataProvider.readResponseTestError
import util.DataProvider.updateRequestTestConcurrentModification
import util.DataProvider.updateRequestTestError
import util.DataProvider.updateResponseTestConcurrentModification
import util.DataProvider.updateResponseTestError
import util.post
import util.testApplication

class ApiErrorInMemoryTest : FunSpec ({

    test("Create request error in memory: already exists") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects
        )

        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(createRequestTestError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe createResponseTestError
        }
    }

    test("Read request error in memory: not found") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitReadObjects.initObjects
        )

        testApplication(meaningRepoInMemory) {
            val response = post("/api/v1/meaning/read") {
                contentType(ContentType.Application.Json)
                setBody(readRequestTestError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningReadResponse shouldBe readResponseTestError
        }
    }

    test("Update request error in memory: not found") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitUpdateObjects.initObjects
        )

        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(updateRequestTestError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponseTestError
        }
    }

    test("Update request error in memory: concurrent modification") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitUpdateObjects.initObjects
        )

        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(updateRequestTestConcurrentModification)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponseTestConcurrentModification
        }
    }

    test("Delete request error in memory: not found") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitDeleteObjects.initObjects
        )

        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(deleteRequestTestError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponseTestError
        }
    }

    test("Delete request error in memory: concurrent modification") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitDeleteObjects.initObjects
        )

        testApplication(meaningRepoInMemory) {token ->
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(deleteRequestTestConcurrentModification)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponseTestConcurrentModification
        }
    }

})