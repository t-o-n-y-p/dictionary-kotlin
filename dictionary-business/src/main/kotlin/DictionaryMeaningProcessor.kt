package com.tonyp.dictionarykotlin.business

import com.tonyp.dictionarykotlin.business.sequences.*
import com.tonyp.dictionarykotlin.business.workers.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.cor.chain

class DictionaryMeaningProcessor {

    suspend fun exec(ctx: DictionaryContext) = BUSINESS_CHAIN.exec(ctx)

    companion object {
        private val BUSINESS_CHAIN = chain {
            initStatus()
            operation("Создание значения слова", DictionaryCommand.CREATE) {
                stubs {
                    stubCreateSuccess()
                    stubCreateError()
                    stubDoesNotExist()
                }
                validation {
                    startMeaningValidation()
                    clearValidatingCreateContext()
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
                    stubDoesNotExist()
                }
                validation {
                    startMeaningValidation()
                    clearValidatingReadContext()
                    validateIdNotEmpty()
                    validateIdContent()
                    finishMeaningValidation()
                }
            }
            operation("Изменение значения слова", DictionaryCommand.UPDATE) {
                stubs {
                    stubUpdateSuccess()
                    stubUpdateError()
                    stubDoesNotExist()
                }
                validation {
                    startMeaningValidation()
                    clearValidatingUpdateContext()
                    validateApprovedNotEmpty()
                    finishMeaningValidation()
                }
            }
            operation("Удаление значения слова", DictionaryCommand.DELETE) {
                stubs {
                    stubDeleteSuccess()
                    stubDeleteError()
                    stubDoesNotExist()
                }
                validation {
                    startMeaningValidation()
                    clearValidatingDeleteContext()
                    validateIdNotEmpty()
                    validateIdContent()
                    finishMeaningValidation()
                }
            }
            operation("Поиск значения слова", DictionaryCommand.SEARCH) {
                stubs {
                    stubSearchSuccess()
                    stubSearchError()
                    stubDoesNotExist()
                }
                validation {
                    startFilterValidation()
                    clearValidatingSearchContext()
                    finishFilterValidation()
                }
            }
        }.build()
    }

}