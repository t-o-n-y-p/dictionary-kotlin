import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.log.v1.models.DictionaryLogModel
import com.tonyp.dictionarykotlin.log.v1.models.ErrorLogModel
import com.tonyp.dictionarykotlin.log.v1.models.MeaningFilterLog
import com.tonyp.dictionarykotlin.log.v1.models.MeaningLog
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ContextToLogMapperTest : FunSpec ({

    val emptyContext = DictionaryContext()
    val crudContext = DictionaryContext(
        command = DictionaryCommand.CREATE,
        requestId = DictionaryRequestId("123"),
        meaningRequest = DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE
        ),
        meaningResponse = DictionaryMeaning(
            id = DictionaryMeaningId("789"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.TRUE
        )
    )
    val searchContext = DictionaryContext(
        command = DictionaryCommand.SEARCH,
        requestId = DictionaryRequestId("456"),
        meaningFilterRequest = DictionaryMeaningFilter(
            word = "обвал",
            approved = DictionaryMeaningApproved.TRUE
        ),
        meaningsResponse = mutableListOf(
            DictionaryMeaning(
                id = DictionaryMeaningId("789"),
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = DictionaryMeaningApproved.FALSE
            ),
            DictionaryMeaning(
                id = DictionaryMeaningId("123"),
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest",
                approved = DictionaryMeaningApproved.TRUE
            ),
        )
    )
    val errorContext = DictionaryContext(
        command = DictionaryCommand.READ,
        requestId = DictionaryRequestId("789"),
        errors = mutableListOf(
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

    test("Map empty context") {
        val log = emptyContext.toLog("empty-context")

        log.messageTime shouldNotBe null
        log.logId shouldBe "empty-context"
        log.source shouldBe "dictionary"
        log.meaning shouldBe null
        log.errors shouldBe emptyList()
    }

    test("Map CRUD context") {
        val log = crudContext.toLog("crud-context")

        log.messageTime shouldNotBe null
        log.logId shouldBe "crud-context"
        log.source shouldBe "dictionary"
        log.meaning shouldBe DictionaryLogModel(
            requestId = "123",
            operation = DictionaryLogModel.Operation.CREATE,
            requestMeaning = MeaningLog(
                id = "456",
                word = "обвал",
                value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                proposedBy = "t-o-n-y-p",
                approved = false
            ),
            responseMeaning = MeaningLog(
                id = "789",
                word = "трава",
                value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                proposedBy = "unittest",
                approved = true
            )
        )
        log.errors shouldBe emptyList()
    }

    test("Map search context") {
        val log = searchContext.toLog("search-context")

        log.messageTime shouldNotBe null
        log.logId shouldBe "search-context"
        log.source shouldBe "dictionary"
        log.meaning shouldBe DictionaryLogModel(
            requestId = "456",
            operation = DictionaryLogModel.Operation.SEARCH,
            requestFilter = MeaningFilterLog(
                word = "обвал",
                approved = true
            ),
            responseMeanings = listOf(
                MeaningLog(
                    id = "789",
                    word = "обвал",
                    value = "снежные глыбы или обломки скал, обрушившиеся с гор",
                    proposedBy = "t-o-n-y-p",
                    approved = false
                ),
                MeaningLog(
                    id = "123",
                    word = "трава",
                    value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
                    proposedBy = "unittest",
                    approved = true
                )
            )
        )
        log.errors shouldBe emptyList()
    }

    test("Map error context") {
        val log = errorContext.toLog("error-context")

        log.messageTime shouldNotBe null
        log.logId shouldBe "error-context"
        log.source shouldBe "dictionary"
        log.meaning shouldBe DictionaryLogModel(
            requestId = "789",
            operation = DictionaryLogModel.Operation.READ
        )
        log.errors shouldBe listOf(
            ErrorLogModel(
                message = "exception",
                code = "123",
                level = "ERROR"
            ),
            ErrorLogModel(
                message = "error",
                code = "456",
                level = "ERROR"
            )
        )
    }


})