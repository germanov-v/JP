plugins{
    `java-library`
}


dependencies{
    api(project(":application"))

    // db
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    //implementation("org.liquibase:liquibase-core:4.29.2")
    // migrations
    implementation("org.liquibase:liquibase-core")
    //implementation("org.springframework.boot:spring-boot-starter-liquibase")

    //
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // for integration tests
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("com.h2database:h2")

    compileOnly("org.springframework.boot:spring-boot-starter-liquibase")
    testImplementation("org.springframework.boot:spring-boot-starter-liquibase")
}