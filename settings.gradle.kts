rootProject.name = "dictionary-kotlin"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val ktorPluginVersion: String by settings
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("io.ktor.plugin") version ktorPluginVersion apply false

        id("org.openapi.generator") version openapiVersion apply false

        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
    }
}
include("dictionary-api-v1")
include("dictionary-common")
include("dictionary-mappers-v1")
include("meaning-app-ktor")
include("dictionary-stubs")
include("dictionary-log-v1")
include("dictionary-log-common")
include("dictionary-log-logback")
