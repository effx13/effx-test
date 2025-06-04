plugins {
    id("effx.spring-conventions")
    id("java-test-fixtures")
}

dependencies {
    api(project(":effx-common"))
    implementation("org.springframework.data:spring-data-commons")
}