package com.tonyp.dictionarykotlin.business

import com.tonyp.dictionarykotlin.business.sequences.operation
import com.tonyp.dictionarykotlin.business.sequences.stubs
import com.tonyp.dictionarykotlin.business.workers.*
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.cor.chain

class DictionaryMeaningProcessor {

    suspend fun exec(ctx: DictionaryContext) = BUSINESS_CHAIN.exec(ctx)

    companion object {
        private val BUSINESS_CHAIN = chain {
            initStatus("Инициализация статуса")
            operation("Создание значения слова", DictionaryCommand.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubCreateError("Имитация неуспешной обработки")
                }
            }
            operation("Получение значения слова", DictionaryCommand.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubReadError("Имитация неуспешной обработки")
                }
            }
            operation("Изменение значения слова", DictionaryCommand.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubUpdateError("Имитация неуспешной обработки")
                }
            }
            operation("Удаление значения слова", DictionaryCommand.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubDeleteError("Имитация неуспешной обработки")
                }
            }
            operation("Поиск значения слова", DictionaryCommand.SEARCH) {
                stubs("Обработка стабов") {
                    stubSearchSuccess("Имитация успешной обработки")
                    stubSearchError("Имитация неуспешной обработки")
                }
            }
        }.build()
    }

}