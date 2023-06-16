plugins {
    kotlin("jvm")
}


dependencies {
    val kotestVersion: String by project
    val datetimeVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    implementation(project(mapOf("path" to ":dictionary-lib-cor")))
    implementation(project(mapOf("path" to ":dictionary-common")))
    implementation(project(mapOf("path" to ":dictionary-log-common")))
    implementation(project(mapOf("path" to ":dictionary-stubs")))
    implementation(project(mapOf("path" to ":dictionary-auth")))

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation(project(mapOf("path" to ":dictionary-repo-in-memory")))
    testImplementation(project(mapOf("path" to ":dictionary-repo-stubs")))
    testImplementation(project(mapOf("path" to ":dictionary-repo-tests")))
}
