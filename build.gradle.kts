import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = "ru.hse"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

ktlint {
    enableExperimentalRules.set(true)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
