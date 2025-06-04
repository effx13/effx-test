import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("effx.dependency-conventions")
    id("org.springframework.boot")
    id("java-library")
    // id("org.jlleitschuh.gradle.ktlint")
    java
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.noarg")
}

noArg {
    annotation("com.oasis.common.projections.NoArgs")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs.plus("-Xjsr305=strict")
        freeCompilerArgs.plus("-Xjvm-default=enable")
        freeCompilerArgs.plus("-progressive")
        freeCompilerArgs.plus("-XXLanguage:+InlineClasses")
    }
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<BootJar> {
    enabled = false
}

tasks.withType<Test> {
    failFast = true

    useJUnitPlatform()
    extensions.configure(JacocoTaskExtension::class) {
        setDestinationFile(
            layout.buildDirectory
                .file("jacoco/jacoco.exec")
                .get()
                .asFile,
        )
    }
    finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<JacocoReport> {
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(false)
    }
    finalizedBy("jacocoTestCoverageVerification")
}

tasks.withType<JacocoCoverageVerification> {
    violationRules {
        rule {
            enabled = false
            element = "BUNDLE" //  룰을 체크할 단위(CLASS, BUNDLE)
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.1".toBigDecimal()
            }
        }
    }
}

dependencies {
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.hibernate.validator:hibernate-validator")

    implementation("io.github.oshai:kotlin-logging")
    implementation("io.github.oshai:kotlin-logging-jvm")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    testImplementation("io.kotest:kotest-runner-junit5-jvm")
    testImplementation("io.kotest:kotest-assertions-core-jvm")
    testImplementation("io.kotest:kotest-assertions-json-jvm")
    testImplementation("io.kotest:kotest-property-jvm")
    testImplementation("io.kotest:kotest-framework-datatest-jvm")
    testImplementation("io.mockk:mockk")
    testImplementation("com.ninja-squad:springmockk")

    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}