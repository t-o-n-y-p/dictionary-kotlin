plugins {
    kotlin("jvm")
}
dependencies {
    val kotestVersion: String by project
    val coroutinesVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(mapOf("path" to ":dictionary-common")))

    implementation(kotlin("test-junit5"))
    implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    implementation("io.kotest:kotest-assertions-core:$kotestVersion")
    implementation(project(mapOf("path" to ":dictionary-stubs")))
}
