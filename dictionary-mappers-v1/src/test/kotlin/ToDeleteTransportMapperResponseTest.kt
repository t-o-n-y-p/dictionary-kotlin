import com.tonyp.dictionarykotlin.api.v1.models.Error
import com.tonyp.dictionarykotlin.api.v1.models.MeaningDeleteResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningResponseDeleteObject
import com.tonyp.dictionarykotlin.api.v1.models.ResponseResult
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToDeleteTransportMapperResponseTest : FunSpec ({

    val emptyDeleteContext = DictionaryContext(
        command = DictionaryCommand.DELETE
    )
    val successDeleteContext = DictionaryContext(
        command = DictionaryCommand.DELETE,
        state = DictionaryState.FINISHING,
        requestId = DictionaryRequestId("456"),
        meaningResponse = DictionaryMeaning(
            id = DictionaryMeaningId("789"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion("qwerty")
        )
    )
    val errorDeleteContext = DictionaryContext(
        command = DictionaryCommand.DELETE,
        state = DictionaryState.FAILING,
        requestId = DictionaryRequestId("789"),
        errors = mutableListOf(
            DictionaryError(),
            DictionaryError(
                code = "123",
                message = "exception"
            ),
            DictionaryError(
                code = "456",
                message = "error"
            )
        )
    )

    test("To transport empty delete response") {
        val req = emptyDeleteContext.toTransportMeaning() as MeaningDeleteResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseDeleteObject()
    }

    test("To transport success delete response") {
        val req = successDeleteContext.toTransportMeaning() as MeaningDeleteResponse

        req.responseType shouldBe null
        req.requestId shouldBe "456"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseDeleteObject(
            id = "789"
        )
    }

    test("To transport error delete response") {
        val req = errorDeleteContext.toTransportMeaning() as MeaningDeleteResponse

        req.responseType shouldBe null
        req.requestId shouldBe "789"
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe listOf(
            Error(),
            Error(
                code = "123",
                message = "exception"
            ),
            Error(
                code = "456",
                message = "error"
            )
        )
        req.meaning shouldBe MeaningResponseDeleteObject()
    }

})