import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToSearchInitTransportMapperResponseTest : FunSpec ({

    val emptySearchInitContext = DictionaryContext(
        command = DictionaryCommand.SEARCH_INIT
    )
    val successSearchInitContext = DictionaryContext(
        command = DictionaryCommand.SEARCH_INIT,
        state = DictionaryState.RUNNING,
        requestId = DictionaryRequestId("456"),
        meaningsResponse = mutableListOf(
            DictionaryMeaning(),
            DictionaryMeaning(
                id = DictionaryMeaningId("123"),
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = DictionaryMeaningApproved.TRUE
            ),
            DictionaryMeaning(
                id = DictionaryMeaningId("789"),
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest",
                approved = DictionaryMeaningApproved.FALSE
            ),
        ),
        webSocketExtensions = mutableListOf(DictionaryWebSocketExtension.DEFLATE, DictionaryWebSocketExtension.NONE)
    )
    val errorSearchInitContext = DictionaryContext(
        command = DictionaryCommand.SEARCH_INIT,
        state = DictionaryState.FAILING,
        requestId = DictionaryRequestId("789"),
        errors = mutableListOf(
            DictionaryError(),
            DictionaryError(
                code = "456",
                message = "exception"
            ),
            DictionaryError(
                code = "123",
                message = "error"
            )
        )
    )

    test("To transport empty search init response") {
        val req = emptySearchInitContext.toTransportMeaning() as MeaningSearchInitResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.meanings shouldBe null
        req.webSocketExtensions shouldBe emptyList()
    }

    test("To transport success search init response") {
        val req = successSearchInitContext.toTransportMeaning() as MeaningSearchInitResponse

        req.responseType shouldBe null
        req.requestId shouldBe "456"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.meanings shouldBe listOf(
            MeaningResponseFullObject(),
            MeaningResponseFullObject(
                id = "123",
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = true
            ),
            MeaningResponseFullObject(
                id = "789",
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest",
                approved = false
            )
        )
        req.webSocketExtensions shouldBe listOf(MeaningWebSocketExtension.DEFLATE)
    }

    test("To transport error search init response") {
        val req = errorSearchInitContext.toTransportMeaning() as MeaningSearchInitResponse

        req.responseType shouldBe null
        req.requestId shouldBe "789"
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe listOf(
            Error(),
            Error(
                code = "456",
                message = "exception"
            ),
            Error(
                code = "123",
                message = "error"
            )
        )
        req.meanings shouldBe null
        req.webSocketExtensions shouldBe emptyList()
    }

})