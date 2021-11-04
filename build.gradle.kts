import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    jacoco
}

group = "ru.hse"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.31")
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.test {
    useJUnit()
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }
    dependsOn(tasks.test)
}

ktlint {
    outputToConsole.set(true)
    enableExperimentalRules.set(true)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
