rootProject.name = "dictionary-kotlin"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
include("dictionary-api-v1")
include("dictionary-common")
include("dictionary-mappers-v1")
