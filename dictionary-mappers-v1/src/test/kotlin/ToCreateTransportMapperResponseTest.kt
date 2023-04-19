import com.tonyp.dictionarykotlin.api.v1.models.Error
import com.tonyp.dictionarykotlin.api.v1.models.MeaningCreateResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionarykotlin.api.v1.models.ResponseResult
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToCreateTransportMapperResponseTest : FunSpec ({

    val emptyCreateContext = DictionaryContext(
        command = DictionaryCommand.CREATE
    )
    val successCreateContext = DictionaryContext(
        command = DictionaryCommand.CREATE,
        state = DictionaryState.FINISHING,
        requestId = DictionaryRequestId("123"),
        meaningResponse = DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE
        )
    )
    val errorCreateContext = DictionaryContext(
        command = DictionaryCommand.CREATE,
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

    test("To transport empty create response") {
        val req = emptyCreateContext.toTransportMeaning() as MeaningCreateResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseFullObject()
    }

    test("To transport success create response") {
        val req = successCreateContext.toTransportMeaning() as MeaningCreateResponse

        req.responseType shouldBe null
        req.requestId shouldBe "123"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseFullObject(
            id = "456",
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = false
        )
    }

    test("To transport error create response") {
        val req = errorCreateContext.toTransportMeaning() as MeaningCreateResponse

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
        req.meaning shouldBe MeaningResponseFullObject()
    }

})