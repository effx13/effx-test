plugins {
    id("effx.spring-conventions")
    id("java-test-fixtures")
}

dependencies {
    implementation(project(":effx-domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")

    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.10.0")
}