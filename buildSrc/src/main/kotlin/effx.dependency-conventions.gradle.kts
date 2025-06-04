plugins {
    id("io.spring.dependency-management")
    id("jacoco")
    `project-report`
    idea
}

repositories {
    gradlePluginPortal() // https://plugins.gradle.org/m2/
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
    }

    dependencies {
        dependencySet("io.github.oshai:6.0.1") {
            entry("kotlin-logging")
            entry("kotlin-logging-jvm")
            entry("kotlin-logging-common")
        }
        dependencySet("io.mockk:1.13.8") {
            entry("mockk")
        }
        dependencySet("io.kotest.extensions:1.1.3") {
            entry("kotest-extensions-spring")
        }
        dependencySet("io.kotest:5.8.0") {
            entry("kotest-runner-junit5-jvm")
            entry("kotest-assertions-core-jvm")
            entry("kotest-assertions-json-jvm")
            entry("kotest-property-jvm")
            entry("kotest-framework-datatest-jvm")
        }
        dependencySet("com.ninja-squad:4.0.2") {
            entry("springmockk")
        }
        dependencySet("io.jsonwebtoken:0.12.6") {
            entry("jjwt-api")
            entry("jjwt-impl")
            entry("jjwt-jackson")
        }
    }
}