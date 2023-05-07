package util

import com.tonyp.dictionarykotlin.api.v1.models.*

@DslMarker
annotation class SearchRequestDsl

@SearchRequestDsl
fun searchRequest(block: SearchRequestBuilder.() -> Unit) = SearchRequestBuilder().apply(block).build()

@SearchRequestDsl
class SearchRequestBuilder {

    private var word: String? = null
    private var approved: Boolean? = null

    @SearchRequestDsl
    fun meaningFilter(block: MeaningFilterContext.() -> Unit) {
        val ctx = MeaningFilterContext().apply(block)

        word = ctx.word
        approved = ctx.approved
    }

    fun build() = MeaningSearchRequest(
        meaningFilter = MeaningSearchFilter(
            word = word,
            approved = approved
        )
    )
}

@SearchRequestDsl
class MeaningFilterContext {
    var word: String? = null
    var approved: Boolean? = null
}