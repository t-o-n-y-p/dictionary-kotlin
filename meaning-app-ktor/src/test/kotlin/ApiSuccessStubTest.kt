import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.repo.stub.MeaningRepoStub
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import util.DataProvider.createRequestStubSuccess
import util.DataProvider.createResponseStubSuccess
import util.DataProvider.deleteRequestStubSuccess
import util.DataProvider.deleteResponseStubSuccess
import util.DataProvider.readRequestStubSuccess
import util.DataProvider.readResponseStubSuccess
import util.DataProvider.searchRequestStubSuccess
import util.DataProvider.searchResponseStubSuccess
import util.DataProvider.updateRequestStubSuccess
import util.DataProvider.updateResponseStubSuccess
import util.post
import util.testApplication

class ApiSuccessStubTest : FunSpec ({

    test("Create request success stub") {
        testApplication(MeaningRepoStub()) {
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                setBody(createRequestStubSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe createResponseStubSuccess
        }
    }

    test("Delete request success stub") {
        testApplication(MeaningRepoStub()) {
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                setBody(deleteRequestStubSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe deleteResponseStubSuccess
        }
    }

    test("Read request success stub") {
        testApplication(MeaningRepoStub()) {
            val response = post("/api/v1/meaning/read") {
                contentType(ContentType.Application.Json)
                setBody(readRequestStubSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningReadResponse shouldBe readResponseStubSuccess
        }
    }

    test("Update request success stub") {
        testApplication(MeaningRepoStub()) {
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                setBody(updateRequestStubSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe updateResponseStubSuccess
        }
    }

    test("Search request success stub") {
        testApplication(MeaningRepoStub()) {
            val response = post("/api/v1/meaning/search") {
                contentType(ContentType.Application.Json)
                setBody(searchRequestStubSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningSearchResponse shouldBe searchResponseStubSuccess
        }
    }

})