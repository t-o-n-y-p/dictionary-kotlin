rootProject.name = "dictionary-kotlin"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}
include("dictionary-api-v1")
include("dictionary-common")
include("dictionary-mappers-v1")
