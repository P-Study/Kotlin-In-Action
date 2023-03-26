plugins {
    kotlin("jvm") version "1.7.20"
}

group = "org.pteam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")

    // test
    testImplementation ("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation ("io.kotest:kotest-assertions-core:5.5.4")
    testImplementation ("io.kotest:kotest-property:5.5.4")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
