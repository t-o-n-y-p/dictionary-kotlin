package com.tonyp.dictionarykotlin.cor

abstract class CorExec<T>(
    override val title: String,
    override val description: String = "",
    private val blockOn: suspend T.() -> Boolean = { true },
    private val blockExcept: suspend T.(Throwable) -> Unit = {},
): ICorExec<T> {
    protected abstract suspend fun handle(context: T)

    private suspend fun on(context: T): Boolean = context.blockOn()
    private suspend fun except(context: T, e: Throwable) = context.blockExcept(e)

    override suspend fun exec(context: T) {
        if (on(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                except(context, e)
            }
        }
    }
}

@CorDslMarker
abstract class CorExecDsl<T> : ICorExecDsl<T> {
    override var title: String = ""
    override var description: String = ""
    protected var blockOn: suspend T.() -> Boolean = { true }
    protected var blockExcept: suspend T.(e: Throwable) -> Unit = { e: Throwable -> throw e }

    fun on(blockOn: suspend T.() -> Boolean) {
        this.blockOn = blockOn
    }

    fun except(blockExcept: suspend T.(e: Throwable) -> Unit) {
        this.blockExcept = blockExcept
    }
}