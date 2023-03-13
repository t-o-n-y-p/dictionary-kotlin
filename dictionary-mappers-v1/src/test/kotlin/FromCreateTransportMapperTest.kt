import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.NONE
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class FromCreateTransportMapperTest : FunSpec ({

    val emptyCreateRequest = MeaningCreateRequest()
    val successStubCreateRequest = MeaningCreateRequest(
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        )
    )
    val firstCreateRequest = MeaningCreateRequest(
        debug = MeaningDebug(),
        meaning = MeaningCreateObject()
    )
    val secondCreateRequest = MeaningCreateRequest(
        requestId = "123",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD,
            stub = MeaningRequestDebugStubs.CANNOT_CREATE
        ),
        meaning = MeaningCreateObject(
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p"
        )
    )

    test("From transport empty create request") {
        val context = DictionaryContext()
        context.fromTransport(emptyCreateRequest)

        context.command shouldBe DictionaryCommand.CREATE
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

    test("From transport stub success create request") {
        val context = DictionaryContext()
        context.fromTransport(successStubCreateRequest)

        context.command shouldBe DictionaryCommand.CREATE
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

    test("From transport create request with missing fields") {
        val context = DictionaryContext()
        context.fromTransport(firstCreateRequest)

        context.command shouldBe DictionaryCommand.CREATE
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

    test("From transport create request with all fields") {
        val context = DictionaryContext()
        context.fromTransport(secondCreateRequest)

        context.command shouldBe DictionaryCommand.CREATE
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.CANNOT_CREATE

        context.requestId shouldBe DictionaryRequestId("123")
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning(
            id = DictionaryMeaningId.NONE,
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.NONE
        )
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter()
        context.meaningResponse shouldBe DictionaryMeaning()
        context.meaningsResponse shouldBe mutableListOf()
    }

})