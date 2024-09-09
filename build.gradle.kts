plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("jacoco")
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClass = "org.example.ApiRequester"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation ("org.mockito:mockito-inline:4.11.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it).matching {
                exclude("sun/util/**")  // Исключение классов JDK, которые вызывают ошибки
            }
        })
    )
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}