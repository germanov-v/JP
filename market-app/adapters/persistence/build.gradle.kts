plugins{
    `java-library`
}


dependencies{
    implementation(project(":application"))

    // db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    //implementation("org.liquibase:liquibase-core:4.29.2")
    // migrations
  // implementation("org.springframework.boot:spring-boot-starter-liquibase")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")

    //
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // for integration tests
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}