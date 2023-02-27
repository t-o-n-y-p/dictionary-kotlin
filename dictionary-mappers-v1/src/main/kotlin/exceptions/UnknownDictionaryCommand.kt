package com.tonyp.dictionarykotlin.mappers.v1.exceptions

import com.tonyp.dictionarykotlin.common.models.DictionaryCommand

class UnknownDictionaryCommand(cmd: DictionaryCommand)
    : Throwable("Wrong command $cmd at mapping toTransport stage")
