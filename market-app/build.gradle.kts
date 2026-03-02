import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.springframework.boot") version "3.5.11" apply false
}



allprojects {
    group = "ru.yp.marketapp"
    version = "1.0-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

val bootVersion = "3.5.11"
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")

    configure<DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$bootVersion")
        }

        dependencies {
            dependency("org.liquibase:liquibase-core:4.25.1")
            //dependency("org.springframework.boot:spring-boot-starter-liquibase:$bootVersion")
        }

    }

    tasks.withType<Test> { useJUnitPlatform() }
    // tasks.withType<Test>().configureEach { useJUnitPlatform() }
}
