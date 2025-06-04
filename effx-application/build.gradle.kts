plugins {
    id("effx.spring-conventions")
}

dependencies {
    implementation(project(":effx-domain"))
    implementation("org.springframework.boot:spring-boot-starter-aop")

    testImplementation(project(":effx-persistence"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
}