import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile

val ktorVersion: String by project
val logbackVersion: String by project
val kotestVersion: String by project
val ktorKotestExtensionVersion: String by project
val datetimeVersion: String by project
val logbackAppendersVersion: String by project
val fluentdLoggerVersion: String by project

fun ktorServer(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-server-$module:$version"
fun ktorClient(module: String, version: String? = ktorVersion): Any =
    "io.ktor:ktor-client-$module:$version"

plugins {
    kotlin("jvm")
    id("application")
    id("com.bmuschko.docker-java-application")
    id("com.bmuschko.docker-remote-api")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("com.tonyp.dictionarykotlin.meaning.app.ApplicationKt")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(ktorServer("core"))
    implementation(ktorServer("netty"))

    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

    implementation(ktorServer("caching-headers"))
    implementation(ktorServer("call-logging"))
    implementation(ktorServer("auto-head-response"))
    implementation(ktorServer("cors"))
    implementation(ktorServer("default-headers"))
    implementation(ktorServer("websockets"))
    implementation(ktorServer("content-negotiation"))
    implementation(ktorServer("config-yaml"))

    implementation(ktorServer("auth"))
    implementation(ktorServer("auth-jwt"))

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.sndyuk:logback-more-appenders:$logbackAppendersVersion")
    implementation("org.fluentd:fluent-logger:$fluentdLoggerVersion")

    implementation(project(mapOf("path" to ":dictionary-common")))
    implementation(project(mapOf("path" to ":dictionary-api-v1")))
    implementation(project(mapOf("path" to ":dictionary-mappers-v1")))
    implementation(project(mapOf("path" to ":dictionary-stubs")))
    implementation(project(mapOf("path" to ":dictionary-log-logback")))
    implementation(project(mapOf("path" to ":dictionary-log-common")))
    implementation(project(mapOf("path" to ":dictionary-log-v1")))
    implementation(project(mapOf("path" to ":dictionary-log-mappers-v1")))
    implementation(project(mapOf("path" to ":dictionary-business")))
    implementation(project(mapOf("path" to ":dictionary-repo-in-memory")))
    implementation(project(mapOf("path" to ":dictionary-repo-stubs")))
    implementation(project(mapOf("path" to ":dictionary-repo-tests")))

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:$ktorKotestExtensionVersion")
    testImplementation(ktorServer("test-host"))
    testImplementation(ktorClient("content-negotiation"))
    testImplementation(ktorClient("websockets"))
}

tasks {
    val dockerJvmDockerfile by creating(Dockerfile::class) {
        group = "docker"
        from("openjdk:17")
        copyFile("app.jar", "app.jar")
        entryPoint("java", "-Xms256m", "-Xmx512m", "-jar", "/app.jar")
    }
    create("dockerBuildJvmImage", DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerJvmDockerfile)
        doFirst {
            copy {
                from(dockerJvmDockerfile)
                into("${project.buildDir}/docker/app.jar")
            }
        }
        images.add("${project.name}:${project.version}")
    }
}