import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.NONE
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class FromUpdateTransportMapperTest : FunSpec ({

    val emptyUpdateRequest = MeaningUpdateRequest()
    val successStubUpdateRequest = MeaningUpdateRequest(
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        )
    )
    val firstUpdateRequest = MeaningUpdateRequest(
        debug = MeaningDebug(),
        meaning = MeaningUpdateObject()
    )
    val secondUpdateRequest = MeaningUpdateRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.TEST,
            stub = MeaningRequestDebugStubs.CANNOT_UPDATE
        ),
        meaning = MeaningUpdateObject(
            id = "789",
            approved = false,
            version = "qwerty"
        )
    )

    test("From transport empty update request") {
        val context = DictionaryContext()
        context.fromTransport(emptyUpdateRequest)

        context.command shouldBe DictionaryCommand.UPDATE
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

    test("From transport stub success update request") {
        val context = DictionaryContext()
        context.fromTransport(successStubUpdateRequest)

        context.command shouldBe DictionaryCommand.UPDATE
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

    test("From transport update request with missing fields") {
        val context = DictionaryContext()
        context.fromTransport(firstUpdateRequest)

        context.command shouldBe DictionaryCommand.UPDATE
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

    test("From transport update request with all fields") {
        val context = DictionaryContext()
        context.fromTransport(secondUpdateRequest)

        context.command shouldBe DictionaryCommand.UPDATE
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.TEST
        context.stubCase shouldBe DictionaryStub.CANNOT_UPDATE

        context.requestId shouldBe DictionaryRequestId("123")
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning(
            id = DictionaryMeaningId("789"),
            word = "",
            value = "",
            proposedBy = "",
            approved = DictionaryMeaningApproved.FALSE,
            version = DictionaryMeaningVersion("qwerty")
        )
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse.isEmpty() shouldBe true
        context.meaningsResponse shouldBe mutableListOf()
    }

})