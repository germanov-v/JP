plugins {
    id("java")
}

group = "ru.yp.marketapp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
   // implementation("org.liquibase:liquibase-core")
   // implementation("org.springframework.boot:spring-boot-starter-liquibase")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}