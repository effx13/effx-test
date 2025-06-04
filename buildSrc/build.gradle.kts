plugins {
    `kotlin-dsl`
}

val kotlinVersion: String by project
val springBootVersion: String by project

project.logger.info("Kotlin Version -> $kotlinVersion")
project.logger.info("Spring Boot Version -> $springBootVersion")

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")

    implementation("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
    implementation("org.asciidoctor:asciidoctor-gradle-jvm:3.3.2")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.5.0")
    implementation("net.saliman:gradle-properties-plugin:1.5.2")
}