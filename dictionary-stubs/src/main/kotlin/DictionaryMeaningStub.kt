package com.tonyp.dictionarykotlin.stubs

import com.tonyp.dictionarykotlin.common.models.*

object DictionaryMeaningStub {

    private val approvedMeaning: DictionaryMeaning =
        DictionaryMeaning(
            id = DictionaryMeaningId("123"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion("qwerty")
        )
    private val pendingMeaning: DictionaryMeaning =
        DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE,
            version = DictionaryMeaningVersion("asdfgh")
        )

    fun getApproved() = approvedMeaning.copy()

    fun getPending() = pendingMeaning.copy()

    fun getSearchResult() = listOf(getApproved(), getPending())

    fun getLongSearchData() = listOf(
        DictionaryMeaning(
            id = DictionaryMeaningId("123"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion("qwerty")
        ),
        DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE,
            version = DictionaryMeaningVersion("asdfgh")
        ),
        DictionaryMeaning(
            id = DictionaryMeaningId("789"),
            word = "трава",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.FALSE,
            version = DictionaryMeaningVersion("zxcvbn")
        ),
        DictionaryMeaning(
            id = DictionaryMeaningId("34567"),
            word = "обвал",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.TRUE,
            version = DictionaryMeaningVersion("qweasdzxc")
        )
    )

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