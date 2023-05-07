plugins {
    kotlin("jvm")
}

dependencies {
    val uuidVersion: String by project
    val cache4kVersion: String by project

    implementation("com.benasher44:uuid:$uuidVersion")
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation(project(mapOf("path" to ":dictionary-common")))
}