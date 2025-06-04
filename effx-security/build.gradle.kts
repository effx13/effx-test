plugins {
    id("effx.spring-conventions")
}

dependencies {
    implementation(project(":effx-domain"))
    implementation(project(":effx-common"))
    implementation(project(":effx-application"))
    implementation(project(":effx-persistence"))

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")

    testImplementation("org.springframework.security:spring-security-test")
}