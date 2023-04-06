plugins {
    kotlin("jvm")
}

dependencies {
    val logbackVersion: String by project
    val logbackEncoderVersion: String by project
    val logbackKafkaVersion: String by project
    val janinoVersion: String by project
    val kotestVersion: String by project
    val datetimeVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logbackEncoderVersion")
    implementation("com.github.danielwegener:logback-kafka-appender:$logbackKafkaVersion")
    implementation("org.codehaus.janino:janino:$janinoVersion")
    implementation(project(mapOf("path" to ":dictionary-log-common")))

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}