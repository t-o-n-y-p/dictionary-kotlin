import com.tonyp.dictionarykotlin.api.v1.models.Error
import com.tonyp.dictionarykotlin.api.v1.models.MeaningReadResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningResponseFullObject
import com.tonyp.dictionarykotlin.api.v1.models.ResponseResult
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToReadTransportMapperResponseTest : FunSpec ({

    val emptyReadContext = DictionaryContext(
        command = DictionaryCommand.READ
    )
    val successReadContext = DictionaryContext(
        command = DictionaryCommand.READ,
        state = DictionaryState.FINISHING,
        requestId = DictionaryRequestId("789"),
        meaningResponse = DictionaryMeaning(
            id = DictionaryMeaningId("123"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion("zxcvbn")
        )
    )
    val errorReadContext = DictionaryContext(
        command = DictionaryCommand.READ,
        state = DictionaryState.FAILING,
        requestId = DictionaryRequestId("123"),
        errors = mutableListOf(
            DictionaryError(),
            DictionaryError(
                code = "456",
                message = "exception"
            ),
            DictionaryError(
                code = "789",
                message = "error"
            )
        )
    )

    test("To transport empty read response") {
        val req = emptyReadContext.toTransportMeaning() as MeaningReadResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseFullObject()
    }

    test("To transport success read response") {
        val req = successReadContext.toTransportMeaning() as MeaningReadResponse

        req.responseType shouldBe null
        req.requestId shouldBe "789"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.meaning shouldBe MeaningResponseFullObject(
            id = "123",
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = true,
            version = "zxcvbn"
        )
    }

    test("To transport error read response") {
        val req = errorReadContext.toTransportMeaning() as MeaningReadResponse

        req.responseType shouldBe null
        req.requestId shouldBe "123"
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe listOf(
            Error(),
            Error(
                code = "456",
                message = "exception"
            ),
            Error(
                code = "789",
                message = "error"
            )
        )
        req.meaning shouldBe MeaningResponseFullObject()
    }

})