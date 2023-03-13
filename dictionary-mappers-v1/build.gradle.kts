plugins {
    kotlin("jvm")
}


dependencies {
    val kotestVersion: String by project
    val datetimeVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    implementation(project(mapOf("path" to ":dictionary-common")))
    implementation(project(mapOf("path" to ":dictionary-api-v1")))
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}