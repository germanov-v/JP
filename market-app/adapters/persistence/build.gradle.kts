plugins{
    `java-library` // https://docs.gradle.org/current/userguide/java_library_plugin.html

    // BOM
    //id("io.spring.dependency-management") // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
}


dependencies{
    implementation(project(":application"))

    // db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // migrations
    implementation("org.springframework.boot:spring-boot-starter-liquibase")


    //
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // for integration tests
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}