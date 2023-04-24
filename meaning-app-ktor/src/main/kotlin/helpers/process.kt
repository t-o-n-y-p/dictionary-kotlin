package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import toLog

private val processor = DictionaryMeaningProcessor()
suspend fun process(appSettings: DictionaryAppSettings, ctx: DictionaryContext, logId: String) {
    val logger = appSettings.corSettings.loggerProvider.logger(::process)
    logger.doWithLogging {
        logger.info(
            msg = "Started processing $logId request",
            data = ctx.toLog("$logId-request")
        )
        processor.exec(ctx)
        logger.info(
            msg = "Response $logId is ready",
            data = ctx.toLog("$logId-response")
        )
    }
}

