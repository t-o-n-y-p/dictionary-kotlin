package com.tonyp.dictionarykotlin.stubs

import com.tonyp.dictionarykotlin.common.models.DictionaryMeaning
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId

object DictionaryMeaningStub {

    private val approvedMeaning: DictionaryMeaning
        get() = DictionaryMeaning(
            id = DictionaryMeaningId("123"),
            word = "трава",
            value = "о чем-н. не имеющем вкуса, безвкусном (разг.)",
            proposedBy = "unittest",
            approved = DictionaryMeaningApproved.TRUE
        )
    private val pendingMeaning: DictionaryMeaning
        get() = DictionaryMeaning(
            id = DictionaryMeaningId("456"),
            word = "обвал",
            value = "снежные глыбы или обломки скал, обрушившиеся с гор",
            proposedBy = "t-o-n-y-p",
            approved = DictionaryMeaningApproved.FALSE
        )

    fun getApproved() = approvedMeaning.copy()

    fun getPending() = pendingMeaning.copy()

    fun getSearchResult() = listOf(getApproved(), getPending())

}