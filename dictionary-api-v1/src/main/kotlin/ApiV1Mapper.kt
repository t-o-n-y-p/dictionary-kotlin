package com.tonyp.dictionarykotlin.api.v1

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper

val apiV1Mapper: ObjectMapper = JsonMapper.builder()
    .configure(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL, true)
    .serializationInclusion(JsonInclude.Include.NON_NULL)
    .build()