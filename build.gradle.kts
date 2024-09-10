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
    testImplementation ("org.mockito:mockito-inline:5.2.0")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.35.1")
    implementation ("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.withType<Test> {
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it).matching {
                exclude("sun/util/**")
            }
        })
    )
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}