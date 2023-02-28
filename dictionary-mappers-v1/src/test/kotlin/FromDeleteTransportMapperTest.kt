import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.NONE
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class FromDeleteTransportMapperTest : FunSpec ({

    val emptyDeleteRequest = MeaningDeleteRequest()
    val successStubDeleteRequest = MeaningDeleteRequest(
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        )
    )
    val firstDeleteRequest = MeaningDeleteRequest(
        debug = MeaningDebug(),
        meaning = MeaningDeleteObject()
    )
    val secondDeleteRequest = MeaningDeleteRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.TEST,
            stub = MeaningRequestDebugStubs.CANNOT_DELETE
        ),
        meaning = MeaningDeleteObject(
            id = "123"
        )
    )

    test("From transport empty delete request") {
        val context = DictionaryContext()
        context.fromTransport(emptyDeleteRequest)

        context.command shouldBe DictionaryCommand.DELETE
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.NONE

        context.requestId shouldBe DictionaryRequestId.NONE
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning()
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse shouldBe DictionaryMeaning()
        context.meaningsResponse shouldBe mutableListOf()
    }

    test("From transport stub success delete request") {
        val context = DictionaryContext()
        context.fromTransport(successStubDeleteRequest)

        context.command shouldBe DictionaryCommand.DELETE
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.STUB
        context.stubCase shouldBe DictionaryStub.SUCCESS

        context.requestId shouldBe DictionaryRequestId.NONE
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning()
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse shouldBe DictionaryMeaning()
        context.meaningsResponse shouldBe mutableListOf()
    }

    test("From transport delete request with missing fields") {
        val context = DictionaryContext()
        context.fromTransport(firstDeleteRequest)

        context.command shouldBe DictionaryCommand.DELETE
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.NONE

        context.requestId shouldBe DictionaryRequestId.NONE
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning()
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse shouldBe DictionaryMeaning()
        context.meaningsResponse shouldBe mutableListOf()
    }

    test("From transport delete request with all fields") {
        val context = DictionaryContext()
        context.fromTransport(secondDeleteRequest)

        context.command shouldBe DictionaryCommand.DELETE
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.TEST
        context.stubCase shouldBe DictionaryStub.CANNOT_DELETE

        context.requestId shouldBe DictionaryRequestId("456")
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning(
            id = DictionaryMeaningId("123"),
            word = "",
            value = "",
            proposedBy = "",
            approved = DictionaryMeaningApproved.NONE
        )
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse shouldBe DictionaryMeaning()
        context.meaningsResponse shouldBe mutableListOf()
    }

})