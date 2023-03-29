import com.tonyp.dictionarykotlin.api.v1.models.Error
import com.tonyp.dictionarykotlin.api.v1.models.MeaningInitResponse
import com.tonyp.dictionarykotlin.api.v1.models.MeaningWebSocketExtension
import com.tonyp.dictionarykotlin.api.v1.models.ResponseResult
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ToInitTransportMapperResponseTest : FunSpec ({

    val emptyInitContext = DictionaryContext(
        command = DictionaryCommand.INIT
    )
    val successInitContext = DictionaryContext(
        command = DictionaryCommand.INIT,
        state = DictionaryState.RUNNING,
        requestId = DictionaryRequestId("456"),
        webSocketExtensions = mutableListOf(DictionaryWebSocketExtension.DEFLATE, DictionaryWebSocketExtension.NONE)
    )
    val errorInitContext = DictionaryContext(
        command = DictionaryCommand.INIT,
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

    test("To transport empty init response") {
        val req = emptyInitContext.toTransportMeaning() as MeaningInitResponse

        req.responseType shouldBe null
        req.requestId shouldBe null
        req.result shouldBe ResponseResult.ERROR
        req.errors shouldBe null
        req.webSocketExtensions shouldBe emptyList()
    }

    test("To transport success init response") {
        val req = successInitContext.toTransportMeaning() as MeaningInitResponse

        req.responseType shouldBe null
        req.requestId shouldBe "456"
        req.result shouldBe ResponseResult.SUCCESS
        req.errors shouldBe null
        req.webSocketExtensions shouldBe listOf(MeaningWebSocketExtension.DEFLATE)
    }

    test("To transport error init response") {
        val req = errorInitContext.toTransportMeaning() as MeaningInitResponse

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
        req.webSocketExtensions shouldBe emptyList()
    }

})