plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(mapOf("path" to ":dictionary-common")))
    implementation(project(mapOf("path" to ":dictionary-stubs")))
}