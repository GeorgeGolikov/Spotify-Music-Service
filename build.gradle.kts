import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
    kotlin("plugin.jpa") version "1.6.0"
    id("jacoco")
    java
}

group = "ru.spbstu.trkpo"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.gradle.org/gradle/libs-releases-local/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("se.michaelthelin.spotify:spotify-web-api-java:7.0.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.testcontainers:postgresql:1.16.3")
    testImplementation("org.testcontainers:testcontainers:1.16.3")
    testImplementation("org.testcontainers:junit-jupiter:1.16.3")
    testImplementation("com.h2database:h2:2.1.210")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    doLast { // print the report url for easier access
        println("file://${project.rootDir}/build/reports/jacoco/test/html/index.html")
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "**/ru/spbstu/trkpo/musicservice/dto/**",
                        "**/ru/spbstu/trkpo/musicservice/entity/**",
                        "**/ru/spbstu/trkpo/musicservice/dao/**",
                        "**/ru/spbstu/trkpo/musicservice/api/impl/MusicServiceApiAggregator.class",
                        "**/ru/spbstu/trkpo/musicservice/api/impl/SpotifyServiceApiConfig.class",
                        "**/ru/spbstu/trkpo/musicservice/MusicServiceApplicationKt.class",
                        "**/ru/spbstu/trkpo/musicservice/MusicServiceApplication.class"
                    )
                }
            }
        )
    )
}

jacoco {
    toolVersion = "0.8.7"
}

