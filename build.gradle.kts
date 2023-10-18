plugins {
    kotlin("jvm") version "1.9.0"
}

group = "sslabs.jobbank.scrape"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("it.skrape:skrapeit:1.2.2")
    implementation("com.squareup.okhttp3:okhttp:3.10.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}
