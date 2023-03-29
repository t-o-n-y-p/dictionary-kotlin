import com.tonyp.dictionarykotlin.api.v1.models.Error
import com.tonyp.dictionarykotlin.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionarykotlin.api.v1.models.MeaningSearchResponse
import com.tonyp.dictionarykotlin.api.v1.models.ResponseResult
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToSearchTransportMapperResponseTest : FunSpec ({

    val emptySearchContext = DictionaryContext(
        command = DictionaryCommand.SEARCH
    )
    val successSearchContext = DictionaryContext(
        command = DictionaryCommand.SEARCH,
        state = DictionaryState.RUNNING,
        requestId = DictionaryRequestId("123"),
        meaningsResponse = mutableListOf(
            DictionaryMeaning(),
            DictionaryMeaning(
                id = DictionaryMeaningId("456"),
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
        )
    )
    val errorSearchContext = DictionaryContext(
        command = DictionaryCommand.SEARCH,
        state = DictionaryState.FAILING,
        requestId = DictionaryRequestId("456"),
        errors = mutableListOf(
            DictionaryError(),
            DictionaryError(
                code = "789",
                message = "exception"
            ),
            DictionaryError(
                code = "123",
                message = "error"
            )
        )
    )

    test("To transport empty search response") {
        val req = emptySearchContext.toTransportMeaning() as MeaningSearchResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.meanings shouldBe null
    }

    test("To transport success search response") {
        val req = successSearchContext.toTransportMeaning() as MeaningSearchResponse

        req.responseType shouldBe null
        req.requestId shouldBe "123"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.meanings shouldBe listOf(
            MeaningResponseFullObject(),
            MeaningResponseFullObject(
                id = "456",
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
    }

    test("To transport error search response") {
        val req = errorSearchContext.toTransportMeaning() as MeaningSearchResponse

        req.responseType shouldBe null
        req.requestId shouldBe "456"
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe listOf(
            Error(),
            Error(
                code = "789",
                message = "exception"
            ),
            Error(
                code = "123",
                message = "error"
            )
        )
        req.meanings shouldBe null
    }

})