package com.tonyp.dictionarykotlin.cor

@CorDslMarker
fun <T> chain(block: CorChainDsl<T>.() -> Unit): CorChainDsl<T> = CorChainDsl<T>().apply(block)

fun <T> CorChainDsl<T>.sequence(block: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(block))
}

fun <T> CorChainDsl<T>.parallel(block: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>(::executeParallel).apply(block))
}

fun <T> CorChainDsl<T>.worker(block: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(block))
}

fun <T> CorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(CorWorkerDsl<T>().also {
        it.title = title
        it.description = description
        it.handle(blockHandle)
    })
}