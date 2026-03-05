plugins {
	java
	id("org.springframework.boot") version "3.5.11"
	//id("io.spring.dependency-management") version "1.1.7"
	//id("java-library")
//	id("io.spring.dependency-management")
}

group = "ru.yp.marketapp"
version = "0.0.1-SNAPSHOT"
description = "bootstrap"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {


	implementation("org.springframework.boot:spring-boot-starter")
	implementation ("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	//testImplementation("org.springframework.boot:spring-boot-starter-test")
	//testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//implementation("org.springframework.boot:spring-boot-starter-liquibase")

	implementation(project(":application"))
	implementation(project(":adapters:persistence"))

	implementation(project(":adapters:web"))
	implementation(project(":adapters:api"))


	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
	testImplementation(project(":application"))
	testImplementation(project(":adapters:persistence"))
	testImplementation(project(":adapters:persistence"))
	testImplementation(project(":adapters:persistence"))
	testImplementation(project(":adapters:persistence"))
	testImplementation(project(":adapters:persistence"))
	testImplementation(project(":adapters:persistence"))
	testImplementation(project(":adapters:persistence"))
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.liquibase:liquibase-core")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
