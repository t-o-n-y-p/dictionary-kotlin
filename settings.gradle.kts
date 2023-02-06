rootProject.name = "dictionary-kotlin"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
include("dictionary-test-module")
