plugins {
    kotlin("jvm")
}

dependencies {
    val uuidVersion: String by project
    val cache4kVersion: String by project
    val coroutinesVersion: String by project
    val kotestVersion: String by project

    implementation("com.benasher44:uuid:$uuidVersion")
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation(project(mapOf("path" to ":dictionary-common")))
    implementation(project(mapOf("path" to ":dictionary-repo-tests")))

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}