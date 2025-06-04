plugins {
    id("effx.spring-conventions")
}

dependencies {
    implementation(project(":effx-domain"))
    implementation(project(":effx-application"))
    implementation(project(":effx-common"))
    implementation(project(":effx-security"))
    implementation(project(":effx-persistence"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}