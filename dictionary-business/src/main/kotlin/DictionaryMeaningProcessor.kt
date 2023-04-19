package com.tonyp.dictionarykotlin.business

import com.tonyp.dictionarykotlin.business.sequences.operation
import com.tonyp.dictionarykotlin.business.sequences.stubs
import com.tonyp.dictionarykotlin.business.sequences.validation
import com.tonyp.dictionarykotlin.business.workers.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningApproved
import com.tonyp.dictionarykotlin.common.models.DictionaryMeaningId
import com.tonyp.dictionarykotlin.cor.chain
import com.tonyp.dictionarykotlin.cor.worker

class DictionaryMeaningProcessor {

    suspend fun exec(ctx: DictionaryContext) = BUSINESS_CHAIN.exec(ctx)

    companion object {
        private val BUSINESS_CHAIN = chain {
            initStatus()
            operation("Создание значения слова", DictionaryCommand.CREATE) {
                stubs {
                    stubCreateSuccess()
                    stubCreateError()
                }
                validation {
                    startMeaningValidation()
                    worker("Очистка id") { meaningValidating.id = DictionaryMeaningId.NONE }
                    worker("Очистка word") { meaningValidating.word = meaningValidating.word.trim() }
                    worker("Очистка value") { meaningValidating.value = meaningValidating.value.trim() }
                    worker("Очистка proposedBy") { meaningValidating.proposedBy = meaningValidating.proposedBy.trim() }
                    worker("Очистка approved") { meaningValidating.approved = DictionaryMeaningApproved.NONE }
                    validateWordNotEmpty()
                    validateValueNotEmpty()
                    validateWordContent()
                    validateValueContent()
                    validateUsernameContent()
                    finishMeaningValidation()
                }
            }
            operation("Получение значения слова", DictionaryCommand.READ) {
                stubs {
                    stubReadSuccess()
                    stubReadError()
                }
                validation {
                    startMeaningValidation()
                    worker("Очистка id") {
                        meaningValidating.id = DictionaryMeaningId(meaningValidating.id.asString().trim())
                    }
                    validateIdNotEmpty()
                    validateIdContent()
                    finishMeaningValidation()
                }
            }
            operation("Изменение значения слова", DictionaryCommand.UPDATE) {
                stubs {
                    stubUpdateSuccess()
                    stubUpdateError()
                }
                validation {
                    startMeaningValidation()
                    worker("Очистка id") {
                        meaningValidating.id = DictionaryMeaningId(meaningValidating.id.asString().trim())
                    }
                    worker("Очистка word") { meaningValidating.word = "" }
                    worker("Очистка value") { meaningValidating.value = "" }
                    worker("Очистка proposedBy") { meaningValidating.proposedBy = "" }
                    validateApprovedNotEmpty()
                    finishMeaningValidation()
                }
            }
            operation("Удаление значения слова", DictionaryCommand.DELETE) {
                stubs {
                    stubDeleteSuccess()
                    stubDeleteError()
                }
                validation {
                    startMeaningValidation()
                    worker("Очистка id") {
                        meaningValidating.id = DictionaryMeaningId(meaningValidating.id.asString().trim())
                    }
                    validateIdNotEmpty()
                    validateIdContent()
                    finishMeaningValidation()
                }
            }
            operation("Поиск значения слова", DictionaryCommand.SEARCH) {
                stubs {
                    stubSearchSuccess()
                    stubSearchError()
                }
                validation {
                    startFilterValidation()
                    finishFilterValidation()
                }
            }
        }.build()
    }

}