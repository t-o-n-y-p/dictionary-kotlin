package com.tonyp.dictionarykotlin.meaning.app.v1

import com.tonyp.dictionarykotlin.api.v1.models.*
import com.tonyp.dictionarykotlin.common.models.DictionaryCommand
import com.tonyp.dictionarykotlin.log.v1.common.IDictionaryLogWrapper
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import io.ktor.server.application.*

suspend fun ApplicationCall.createMeaning(appSettings: DictionaryAppSettings, logger: IDictionaryLogWrapper) {
    processCommand<MeaningCreateRequest>(
        DictionaryCommand.CREATE, appSettings, logger, "meaning-create"
    )?.also { sendWebSocketCreateDeleteNotification(appSettings, it) }

}

suspend fun ApplicationCall.readMeaning(appSettings: DictionaryAppSettings, logger: IDictionaryLogWrapper) {
    processCommand<MeaningReadRequest>(
        DictionaryCommand.READ, appSettings, logger, "meaning-read"
    )
}

suspend fun ApplicationCall.updateMeaning(appSettings: DictionaryAppSettings, logger: IDictionaryLogWrapper) {
    processCommand<MeaningUpdateRequest>(
        DictionaryCommand.UPDATE, appSettings, logger, "meaning-update"
    )?.also { sendWebSocketUpdateNotification(appSettings, it) }
}

suspend fun ApplicationCall.deleteMeaning(appSettings: DictionaryAppSettings, logger: IDictionaryLogWrapper) {
    processCommand<MeaningDeleteRequest>(
        DictionaryCommand.DELETE, appSettings, logger, "meaning-delete"
    )?.also { sendWebSocketCreateDeleteNotification(appSettings, it) }
}

suspend fun ApplicationCall.searchMeaning(appSettings: DictionaryAppSettings, logger: IDictionaryLogWrapper) {
    processCommand<MeaningSearchRequest>(
        DictionaryCommand.SEARCH, appSettings, logger, "meaning-search"
    )
}