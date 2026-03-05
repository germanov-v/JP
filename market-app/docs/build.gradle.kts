plugins{
    `java-library`
}


dependencies{
    implementation(project(":application"))

    // db

    runtimeOnly("org.postgresql:postgresql")
    //implementation("org.liquibase:liquibase-core:4.29.2")
    // migrations
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    implementation("org.liquibase:liquibase-core")

    //
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // for integration tests
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}