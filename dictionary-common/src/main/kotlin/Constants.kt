package com.tonyp.dictionarykotlin.common

import kotlinx.datetime.Instant

private val instantNone = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = instantNone
