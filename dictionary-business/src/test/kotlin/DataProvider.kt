import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.DictionaryCorSettings
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.common.repo.DbMeaningResponse
import com.tonyp.dictionarykotlin.common.repo.DbMeaningsResponse
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_ALREADY_EXISTS
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_EMPTY_ID
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository.Errors.RESULT_ERROR_NOT_FOUND
import com.tonyp.dictionarykotlin.repo.stub.MeaningRepoStub
import com.tonyp.dictionarykotlin.repo.tests.MeaningRepositoryMock
import com.tonyp.dictionarykotlin.stubs.DictionaryMeaningStub
import io.kotest.data.row

object DataProvider {

    fun processor(repo: IMeaningRepository = MeaningRepoStub()) = DictionaryMeaningProcessor(
        settings = DictionaryCorSettings(
            repositories = mapOf(
                DictionaryWorkMode.PROD to repo,
                DictionaryWorkMode.TEST to repo
            )
        )
    )

    val successRepo = MeaningRepositoryMock(
        invokeCreate = {
            DbMeaningResponse.success(
                DictionaryMeaning(
                    id = DictionaryMeaningId("10000000000000000000000000000001"),
                    word = it.meaning.word,
                    value = it.meaning.value,
                    proposedBy = it.meaning.proposedBy,
                    approved = DictionaryMeaningApproved.FALSE
                )
            )
        },
        invokeRead = { idRequest ->
            DbMeaningResponse.success(
                DictionaryMeaningStub.getSearchResult().first { idRequest.id == it.id }
            )
        },
        invokeUpdate = {
            DbMeaningResponse.success(it.meaning)
        },
        invokeDelete = { idRequest ->
            DbMeaningResponse.success(
                DictionaryMeaningStub.getSearchResult().first { idRequest.id == it.id }
            )
        },
        invokeSearch = {
            DbMeaningsResponse.success(DictionaryMeaningStub.getSearchResult())
        }
    )

    val createReadSearchErrorRepo = MeaningRepositoryMock(
        invokeCreate = {
            RESULT_ERROR_ALREADY_EXISTS
        },
        invokeRead = {
            RESULT_ERROR_NOT_FOUND
        },
        invokeSearch = {
            DbMeaningsResponse.error(DictionaryMeaningStub.getSearchError())
        }
    )

    val updateDeleteErrorRepo = MeaningRepositoryMock(
        invokeRead = { idRequest ->
            DbMeaningResponse.success(
                DictionaryMeaningStub.getSearchResult().first { idRequest.id == it.id }
            )
        },
        invokeUpdate = {
            RESULT_ERROR_EMPTY_ID
        },
        invokeDelete = {
            RESULT_ERROR_NOT_FOUND
        }
    )

    val empties = listOf(
        row("Empty", ""),
        row("Blank", "    ")
    )
    val validWords = listOf(
        row("Я", "Я"),
        row("   Кабо-Верде        ", "   Кабо-Верде        "),
        row("сложносочинённое", "сложносочинённое")
    )
    val invalidWords = listOf(
        row("Non-cyrillic", "English"),
        row("Too long", "метилпропенилендигидроксициннаменилакрил"),
        row("Special symbols", "специальные символы?!")
    )
    val validValues = listOf(
        row("трава", "о чем-н. не имеющем вкуса, безвкусном (разг.)"),
        row("обвал", "снежные глыбы или обломки скал, обрушившиеся с гор"),
        row("митинг", "    произошло от Meeting                  ")
    )
    val validUsernames = listOf(
        row("    t_o_n_y_p                      ", "    t_o_n_y_p                      "),
        row("unittest", "unittest"),
        row("hedgehog1234", "hedgehog1234")
    )
    val invalidUsernames = listOf(
        row("Too long", "hedgehog12345678"),
        row("Special symbols", "special?!"),
        row("Non-latin", "пешеход")
    )
    val validIds = listOf(
        row("0", "0"),
        row("123", "123"),
        row("         456789    ", "         456789    ")
    )
    val invalidIds = listOf(
        row("Latin with numbers", "hedgehog1234"),
        row("Special symbols", "special?!"),
        row("Non-latin", "пешеход")
    )
    val validApproved = listOf(
        row("false", DictionaryMeaningApproved.FALSE),
        row("true", DictionaryMeaningApproved.TRUE)
    )

}