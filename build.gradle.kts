plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.graalvm.buildtools.native") version "0.9.28"
}

group = "io.github.semanticpie.pietunes"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-data-neo4j")

    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("org.springframework.boot:spring-boot-devtools")

    implementation ("org.springdoc:springdoc-openapi-starter-webflux-ui:2.4.0")
    implementation ("org.springdoc:springdoc-openapi-starter-webflux-api:2.4.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
