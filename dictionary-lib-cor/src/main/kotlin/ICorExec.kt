package com.tonyp.dictionarykotlin.cor

interface ICorExec<T> {
    val title: String
    val description: String
    suspend fun exec(context: T)
}

@CorDslMarker
interface ICorExecDsl<T> {
    var title: String
    var description: String

    fun build(): ICorExec<T>
}