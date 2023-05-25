import com.tonyp.dictionarykotlin.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningUpdateResponse
import com.tonyp.dictionarykotlin.common.permissions.DictionaryPrincipal
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.repo.inmemory.MeaningRepoInMemory
import com.tonyp.dictionarykotlin.repo.tests.InitCreateObjects
import com.tonyp.dictionarykotlin.repo.tests.InitDeleteObjects
import com.tonyp.dictionarykotlin.repo.tests.InitUpdateObjects
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import util.DataProvider
import util.post
import util.testApplication

class ApiAuthTest : FunSpec ({

    test("Create request in memory (own, user)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestOwnSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestOwnSuccess
        }
    }

    test("Create request in memory (own, banned user)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.BANNED)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestOwnSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestAccessDenied
        }
    }

    test("Create request in memory (not own, user)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestAccessDenied
        }
    }

    test("Create request in memory (own, admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestOwnSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestOwnSuccess
        }
    }

    test("Create request in memory (own, banned admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestOwnSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestOwnSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestAccessDenied
        }
    }

    test("Create request in memory (not own, admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestSuccess
        }
    }

    test("Create request in memory (not own, banned admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitCreateObjects.initObjects,
            idUuid = { DataProvider.createResponseTestSuccess.meaning!!.id!! },
            versionUuid = { DataProvider.createResponseTestSuccess.meaning!!.version!! }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/create") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.createRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningCreateResponse shouldBe DataProvider.createResponseTestAccessDenied
        }
    }

    test("Delete request in memory (user)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitDeleteObjects.initObjects
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.deleteRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe DataProvider.deleteResponseTestAccessDenied
        }
    }

    test("Delete request in memory (admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitDeleteObjects.initObjects
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.deleteRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe DataProvider.deleteResponseTestSuccess
        }
    }

    test("Delete request in memory (banned admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitDeleteObjects.initObjects
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/delete") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.deleteRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningDeleteResponse shouldBe DataProvider.deleteResponseTestAccessDenied
        }
    }

    test("Update request in memory (user)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitUpdateObjects.initObjects,
            versionUuid = { "db6d3220-cb83-46ba-b074-40e49f2a8c65" }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.updateRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe DataProvider.updateResponseTestAccessDenied
        }
    }

    test("Update request in memory (admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitUpdateObjects.initObjects,
            versionUuid = { "db6d3220-cb83-46ba-b074-40e49f2a8c65" }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.updateRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe DataProvider.updateResponseTestSuccess
        }
    }

    test("Update request in memory (banned admin)") {
        val meaningRepoInMemory = MeaningRepoInMemory(
            initObjects = InitUpdateObjects.initObjects,
            versionUuid = { "db6d3220-cb83-46ba-b074-40e49f2a8c65" }
        )
        val principal = DictionaryPrincipal(
            name = "t_o_n_y_p",
            groups = setOf(DictionaryUserGroup.USER, DictionaryUserGroup.ADMIN, DictionaryUserGroup.BANNED)
        )

        testApplication(meaningRepoInMemory, principal) {token ->
            val response = post("/api/v1/meaning/update") {
                contentType(ContentType.Application.Json)
                headers {
                    bearerAuth(token)
                }
                setBody(DataProvider.updateRequestTestSuccess)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body() as MeaningUpdateResponse shouldBe DataProvider.updateResponseTestAccessDenied
        }
    }

})