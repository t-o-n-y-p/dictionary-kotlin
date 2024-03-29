package com.tonyp.dictionarykotlin.business

import com.tonyp.dictionarykotlin.business.sequences.*
import com.tonyp.dictionarykotlin.business.workers.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.DictionaryCorSettings
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.cor.chain

class DictionaryMeaningProcessor(
    private val settings: DictionaryCorSettings = DictionaryCorSettings()
) {

    suspend fun exec(ctx: DictionaryContext) =
        BUSINESS_CHAIN.exec(ctx.apply { settings = this@DictionaryMeaningProcessor.settings })

    companion object {
        private val BUSINESS_CHAIN = chain {
            initStatus()
            initRepo()

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
                resolvePermissions()
                repo("Логика создания") {
                    repoPrepareCreate()
                    accessValidation()
                    repoCreate()
                }
                prepareResult()
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
                repo("Логика чтения") {
                    repoRead()
                    repoPrepareReadResult()
                }
                prepareResult()
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
                    validateIdNotEmpty()
                    validateIdContent()
                    validateApprovedNotEmpty()
                    validateVersionNotEmpty()
                    validateVersionContent()
                    finishMeaningValidation()
                }
                resolvePermissions()
                repo("Логика изменения") {
                    repoRead()
                    repoPrepareUpdate()
                    accessValidation()
                    repoUpdate()
                }
                prepareResult()
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
                    validateVersionNotEmpty()
                    validateVersionContent()
                    finishMeaningValidation()
                }
                resolvePermissions()
                repo("Логика удаления") {
                    repoRead()
                    repoPrepareDelete()
                    accessValidation()
                    repoDelete()
                }
                prepareResult()
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
                repo("Логика поиска") {
                    repoSearch()
                }
                prepareResult()
            }
        }.build()
    }

}