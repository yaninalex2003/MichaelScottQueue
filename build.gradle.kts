plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:lincheck-jvm:2.34")
    testImplementation("org.mockito:mockito-core:5.1.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}