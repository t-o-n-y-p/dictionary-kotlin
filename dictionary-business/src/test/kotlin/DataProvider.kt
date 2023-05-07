import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import io.kotest.data.row

object DataProvider {

    val processor = DictionaryMeaningProcessor()

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