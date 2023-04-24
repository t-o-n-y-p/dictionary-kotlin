package com.tonyp.dictionarykotlin.stubs

import com.tonyp.dictionarykotlin.common.models.DictionaryError
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId

object DictionaryMeaningStub {

    private val approvedMeaning: DictionaryMeaning =
        DictionaryMeaning(
            id = DictionaryMeaningId("123"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.TRUE
        )
    private val pendingMeaning: DictionaryMeaning =
        DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE
        )

    fun getApproved() = approvedMeaning.copy()

    fun getPending() = pendingMeaning.copy()

    fun getSearchResult() = listOf(getApproved(), getPending())

    fun getCreateError() = DictionaryError(
        code = StubErrorCode.CANNOT_CREATE.name,
        message = "Cannot create"
    )

    fun getReadError() = DictionaryError(
        code = StubErrorCode.CANNOT_READ.name,
        message = "Cannot read"
    )

    fun getUpdateError() = DictionaryError(
        code = StubErrorCode.CANNOT_UPDATE.name,
        message = "Cannot update"
    )

    fun getDeleteError() = DictionaryError(
        code = StubErrorCode.CANNOT_DELETE.name,
        message = "Cannot delete"
    )

    fun getSearchError() = DictionaryError(
        code = StubErrorCode.CANNOT_SEARCH.name,
        message = "Cannot search"
    )

    fun getInvalidStubError() = DictionaryError(
        code = StubErrorCode.INVALID_STUB.name,
        message = "Provided stub does not exist"
    )

}