import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.NONE
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.stubs.DictionaryStub
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant

class FromSearchTransportMapperTest : FunSpec ({

    val emptySearchRequest = MeaningSearchRequest()
    val successStubSearchRequest = MeaningSearchRequest(
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.STUB,
            stub = MeaningRequestDebugStubs.SUCCESS
        )
    )
    val firstSearchRequest = MeaningSearchRequest(
        debug = MeaningDebug(),
        meaningFilter = MeaningSearchFilter()
    )
    val secondSearchRequest = MeaningSearchRequest(
        requestId = "456",
        debug = MeaningDebug(
            mode = MeaningRequestDebugMode.PROD,
            stub = MeaningRequestDebugStubs.CANNOT_SEARCH
        ),
        meaningFilter = MeaningSearchFilter(
            word = "трава",
            approved = true
        )
    )

    test("From transport empty search request") {
        val context = DictionaryContext()
        context.fromTransport(emptySearchRequest)

        context.command shouldBe DictionaryCommand.SEARCH
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

    test("From transport stub success search request") {
        val context = DictionaryContext()
        context.fromTransport(successStubSearchRequest)

        context.command shouldBe DictionaryCommand.SEARCH
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

    test("From transport search request with missing fields") {
        val context = DictionaryContext()
        context.fromTransport(firstSearchRequest)

        context.command shouldBe DictionaryCommand.SEARCH
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

    test("From transport search request with all fields") {
        val context = DictionaryContext()
        context.fromTransport(secondSearchRequest)

        context.command shouldBe DictionaryCommand.SEARCH
        context.state shouldBe DictionaryState.NONE
        context.errors shouldBe mutableListOf()

        context.workMode shouldBe DictionaryWorkMode.PROD
        context.stubCase shouldBe DictionaryStub.CANNOT_SEARCH

        context.requestId shouldBe DictionaryRequestId("456")
        context.timeStart shouldBe Instant.NONE
        context.meaningRequest shouldBe DictionaryMeaning()
        context.meaningFilterRequest shouldBe DictionaryMeaningFilter(
            word = "трава",
            approved = DictionaryMeaningApproved.TRUE
        )
        context.meaningResponse shouldBe DictionaryMeaning()
        context.meaningsResponse shouldBe mutableListOf()
    }

})