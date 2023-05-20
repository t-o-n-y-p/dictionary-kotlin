import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.NONE
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class FromReadTransportMapperTest : FunSpec ({

    val emptyReadRequest = MeaningReadRequest()
    val successStubReadRequest = MeaningReadRequest(
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        )
    )
    val firstReadRequest = MeaningReadRequest(
        debug = MeaningDebug(),
        meaning = MeaningReadObject()
    )
    val secondReadRequest = MeaningReadRequest(
        requestId = "789",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD,
            stub = MeaningRequestDebugStubs.CANNOT_READ
        ),
        meaning = MeaningReadObject(
            id = "456"
        )
    )

    test("From transport empty read request") {
        val context = DictionaryContext()
        context.fromTransport(emptyReadRequest)

        context.command shouldBe DictionaryCommand.READ
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.NONE

        context.requestId shouldBe DictionaryRequestId.NONE
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest.isEmpty() shouldBe true
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse.isEmpty() shouldBe true
        context.meaningsResponse shouldBe mutableListOf()
    }

    test("From transport stub success read request") {
        val context = DictionaryContext()
        context.fromTransport(successStubReadRequest)

        context.command shouldBe DictionaryCommand.READ
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.STUB
        context.stubCase shouldBe DictionaryStub.SUCCESS

        context.requestId shouldBe DictionaryRequestId.NONE
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest.isEmpty() shouldBe true
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse.isEmpty() shouldBe true
        context.meaningsResponse shouldBe mutableListOf()
    }

    test("From transport read request with missing fields") {
        val context = DictionaryContext()
        context.fromTransport(firstReadRequest)

        context.command shouldBe DictionaryCommand.READ
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.NONE

        context.requestId shouldBe DictionaryRequestId.NONE
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest.isEmpty() shouldBe true
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse.isEmpty() shouldBe true
        context.meaningsResponse shouldBe mutableListOf()
    }

    test("From transport read request with all fields") {
        val context = DictionaryContext()
        context.fromTransport(secondReadRequest)

        context.command shouldBe DictionaryCommand.READ
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.CANNOT_READ

        context.requestId shouldBe DictionaryRequestId("789")
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "",
            value = "",
            proposedBy = "",
            approved = DictionaryMeaningApproved.NONE
        )
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse.isEmpty() shouldBe true
        context.meaningsResponse shouldBe mutableListOf()
    }

})