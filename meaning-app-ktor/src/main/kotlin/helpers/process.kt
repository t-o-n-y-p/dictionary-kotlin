package com.tonyp.dictionarykotlin.meaning.app.helpers

import com.tonyp.dictionarykotlin.business.DictionaryMeaningProcessor
import com.tonyp.dictionarykotlin.common.DictionaryContext
import com.tonyp.dictionarykotlin.meaning.app.DictionaryAppSettings
import toLog

private val processor = DictionaryMeaningProcessor()
suspend fun process(appSettings: DictionaryAppSettings, ctx: DictionaryContext) {
    val logger = appSettings.corSettings.loggerProvider.logger(::process)
    logger.doWithLogging {
        logger.info(
            msg = "Started processing ${ctx.command} request",
            data = ctx.toLog("${ctx.command.name.lowercase()}-request")
        )
        processor.exec(ctx)
        logger.info(
            msg = "Response ${ctx.command} is ready",
            data = ctx.toLog("${ctx.command.name.lowercase()}-response")
        )
    }
}

