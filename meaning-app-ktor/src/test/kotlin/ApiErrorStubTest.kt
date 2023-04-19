import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import util.DataProvider
import util.post

class ApiErrorStubTest : FunSpec ({

    test("Create request error stub") {
        testApplication {
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                setBody(DataProvider.createRequestStubError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseStubError
        }
    }

    test("Delete request error stub") {
        testApplication {
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                setBody(DataProvider.deleteRequestStubError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe DataProvider.deleteResponseStubError
        }
    }

    test("Read request error stub") {
        testApplication {
            val response = post("/api/v1/meaning/read") {
                contentType(ContentType.Application.Json)
                setBody(DataProvider.readRequestStubError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningReadResponse shouldBe DataProvider.readResponseStubError
        }
    }

    test("Update request error stub") {
        testApplication {
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                setBody(DataProvider.updateRequestStubError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe DataProvider.updateResponseStubError
        }
    }

    test("Search request error stub") {
        testApplication {
            val response = post("/api/v1/meaning/search") {
                contentType(ContentType.Application.Json)
                setBody(DataProvider.searchRequestStubError)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningSearchResponse shouldBe DataProvider.searchResponseStubError
        }
    }


})