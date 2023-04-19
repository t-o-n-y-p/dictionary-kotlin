import com.tonyp.dictionarykotlin.api.v1.models.Error
import com.tonyp.dictionarykotlin.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionarykotlin.api.v1.models.MeaningUpdateResponse
import com.tonyp.dictionarykotlin.api.v1.models.ResponseResult
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToUpdateTransportMapperResponseTest : FunSpec ({

    val emptyUpdateContext = DictionaryContext(
        command = DictionaryCommand.UPDATE
    )
    val successUpdateContext = DictionaryContext(
        command = DictionaryCommand.UPDATE,
        state = DictionaryState.FINISHING,
        requestId = DictionaryRequestId("123"),
        meaningResponse = DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.FALSE
        )
    )
    val errorUpdateContext = DictionaryContext(
        command = DictionaryCommand.UPDATE,
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

    test("To transport empty update response") {
        val req = emptyUpdateContext.toTransportMeaning() as MeaningUpdateResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseFullObject()
    }

    test("To transport success update response") {
        val req = successUpdateContext.toTransportMeaning() as MeaningUpdateResponse

        req.responseType shouldBe null
        req.requestId shouldBe "123"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseFullObject(
            id = "456",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = false
        )
    }

    test("To transport error update response") {
        val req = errorUpdateContext.toTransportMeaning() as MeaningUpdateResponse

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