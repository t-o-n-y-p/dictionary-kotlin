package com.tonyp.dictionarykotlin.business.sequences

import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.common.helpers.errorAdministration
import com.tonyp.dictionarykotlin.common.helpers.fail
import com.tonyp.dictionarykotlin.common.models.DictionaryState
import com.tonyp.dictionarykotlin.common.models.DictionaryWorkMode
import com.tonyp.dictionarykotlin.common.permissions.DictionaryUserGroup
import com.tonyp.dictionarykotlin.common.repo.IMeaningRepository
import com.tonyp.dictionarykotlin.cor.CorChainDsl
import com.tonyp.dictionarykotlin.cor.sequence
import com.tonyp.dictionarykotlin.cor.worker

fun CorChainDsl<DictionaryContext>.initRepo() = sequence {
    this.title = "Инициализация репозитория"
    on { state == DictionaryState.RUNNING }
    worker("Выбор репозитория по режиму работы и принсипалу") {
        principal
            .takeIf { it.groups.contains(DictionaryUserGroup.TEST) }
            ?.let { settings.repositories[DictionaryWorkMode.TEST] }
            ?: settings.repositories[workMode]
            ?: IMeaningRepository.NONE
    }
    worker {
        this.title = "Проверка конфигурации репозитория"
        on { workMode != DictionaryWorkMode.STUB && meaningRepo == IMeaningRepository.NONE }
        handle {
            fail(
                errorAdministration(
                    code = "DATABASE_NOT_CONFIGURED",
                    message = "The database is not configured for the work mode ($workMode). " +
                            "Please contact the administrator"
                )
            )
        }
    }
}