plugins {
    id("java")
}

group = "ru.yp.marketapp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    api(project(":application"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}