import com.tonyp.dictionarykotlin.api.v1.models.IRequest
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.mappers.v1.exceptions.UnknownDictionaryCommand
import com.tonyp.dictionarykotlin.mappers.v1.exceptions.UnknownRequestClass
import com.tonyp.dictionarykotlin.mappers.v1.fromTransport
import com.tonyp.dictionarykotlin.mappers.v1.toTransportMeaning
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldMatch

class UnknownTransportMapperTest : FunSpec ({

    val emptyUnknownRequest = object : IRequest {
        override var requestType = "unknown"
        override var requestId = "123"
    }

    test("From transport unknown request type") {
        val exc = shouldThrowExactly<UnknownRequestClass> {
            DictionaryContext().fromTransport(emptyUnknownRequest)
        }
        exc.message shouldMatch Regex("Class .* cannot be mapped to context")
    }

    test("To transport unknown request type") {
        val exc = shouldThrowExactly<UnknownDictionaryCommand> {
            DictionaryContext().toTransportMeaning()
        }
        exc.message shouldMatch Regex("Wrong command .* at mapping toTransport stage")
    }

})