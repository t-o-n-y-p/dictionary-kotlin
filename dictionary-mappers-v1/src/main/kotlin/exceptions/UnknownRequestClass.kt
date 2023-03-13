package com.tonyp.dictionarykotlin.mappers.v1.exceptions

class UnknownRequestClass(javaClass: Class<*>)
    : Throwable("Class $javaClass cannot be mapped to context")
