package com.tonyp.dictionarykotlin.cor

class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},
    private val blockHandle: suspend T.() -> Unit = {},
) : CorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) = blockHandle(context)
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>() {
    private var blockHandle: suspend T.() -> Unit = {}
    fun handle(blockHandle: suspend T.() -> Unit) {
        this.blockHandle = blockHandle
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockExcept = blockExcept,
        blockHandle = blockHandle
    )

}
