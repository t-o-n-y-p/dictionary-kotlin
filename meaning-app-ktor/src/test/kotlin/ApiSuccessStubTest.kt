import com.tonyp.dictionarykotlin.api.v1.models.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import util.DataProvider.createRequest
import util.DataProvider.createResponse
import util.DataProvider.deleteRequest
import util.DataProvider.deleteResponse
import util.DataProvider.readRequest
import util.DataProvider.readResponse
import util.DataProvider.searchRequest
import util.DataProvider.searchResponse
import util.DataProvider.updateRequest
import util.DataProvider.updateResponse
import util.post

class ApiSuccessStubTest : FunSpec ({

    test("Create request success stub") {
        testApplication {
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                setBody(createRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe createResponse
        }
    }

    test("Delete request success stub") {
        testApplication {
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                setBody(deleteRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponse
        }
    }

    test("Read request success stub") {
        testApplication {
            val response = post("/api/v1/meaning/read") {
                contentType(ContentType.Application.Json)
                setBody(readRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningReadResponse shouldBe readResponse
        }
    }

    test("Update request success stub") {
        testApplication {
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                setBody(updateRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponse
        }
    }

    test("Search request success stub") {
        testApplication {
            val response = post("/api/v1/meaning/search") {
                contentType(ContentType.Application.Json)
                setBody(searchRequest)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningSearchResponse shouldBe searchResponse
        }
    }

})