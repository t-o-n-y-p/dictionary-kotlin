import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.*
import com.tonyp.dictionarykotlin.log.v1.models.*
import kotlinx.datetime.Clock

fun DictionaryContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "dictionary",
    meaning = toMeaningLog(),
    errors = errors.map { it.toLog() }
)

private fun DictionaryContext.toMeaningLog(): DictionaryLogModel? {
    val empty = DictionaryMeaning()
    return DictionaryLogModel(
        requestId = requestId.takeIf { it != DictionaryRequestId.NONE }?.asString(),
        operation = command.toLog(),
        requestMeaning = meaningRequest.takeIf { it != empty }?.toLog(),
        responseMeaning = meaningResponse.takeIf { it != empty }?.toLog(),
        responseMeanings = meaningsResponse.takeIf { it.isNotEmpty() }?.filter { it != empty }?.map { it.toLog() },
        requestFilter = meaningFilterRequest.takeIf { it != DictionaryMeaningFilter() }?.toLog(),
    ).takeIf { it != DictionaryLogModel() }
}

private fun DictionaryCommand.toLog() = when (this) {
    DictionaryCommand.CREATE -> DictionaryLogModel.Operation.CREATE
    DictionaryCommand.READ -> DictionaryLogModel.Operation.READ
    DictionaryCommand.UPDATE -> DictionaryLogModel.Operation.UPDATE
    DictionaryCommand.DELETE -> DictionaryLogModel.Operation.DELETE
    DictionaryCommand.SEARCH -> DictionaryLogModel.Operation.SEARCH
    DictionaryCommand.INIT -> DictionaryLogModel.Operation.INIT
    DictionaryCommand.NONE -> null
}

private fun DictionaryMeaningFilter.toLog() = MeaningFilterLog(
    word = word.takeIf { it.isNotBlank() },
    approved = approved.toLog()
)

private fun DictionaryMeaning.toLog() = MeaningLog(
    id = id.takeIf { it != DictionaryMeaningId.NONE }?.asString(),
    word = word.takeIf { it.isNotBlank() },
    value = value.takeIf { it.isNotBlank() },
    proposedBy = proposedBy.takeIf { it.isNotBlank() },
    approved = approved.toLog()
)

private fun DictionaryMeaningApproved.toLog(): Boolean? = when (this) {
    DictionaryMeaningApproved.FALSE -> false
    DictionaryMeaningApproved.TRUE -> true
    DictionaryMeaningApproved.NONE -> null
}

private fun DictionaryError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name
)