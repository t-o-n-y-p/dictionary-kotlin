package com.tonyp.dictionarykotlin.cor

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CorChain<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},
    private val handler: suspend (T, List<ICorExec<T>>) -> Unit,
    private val execs: List<ICorExec<T>>,
) : CorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) = handler(context, execs)
}

suspend fun <T> executeSequential(context: T, execs: List<ICorExec<T>>): Unit =
    execs.forEach {
        it.exec(context)
    }

suspend fun <T> executeParallel(context: T, execs: List<ICorExec<T>>): Unit = coroutineScope {
    execs.forEach {
        launch { it.exec(context) }
    }
}

@CorDslMarker
class CorChainDsl<T>(
    private val handler: suspend (T, List<ICorExec<T>>) -> Unit = ::executeSequential,
) : CorExecDsl<T>() {
    private val execs: MutableList<ICorExecDsl<T>> = mutableListOf()
    fun add(exec: ICorExecDsl<T>) {
        execs.add(exec)
    }

    override fun build(): ICorExec<T> = CorChain(
        title = title,
        description = description,
        blockOn = blockOn,
        blockExcept = blockExcept,
        handler = handler,
        execs = execs.map { it.build() }
    )
}
