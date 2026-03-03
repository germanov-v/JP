allprojects {
    group = "ru.yp.marketapp"
    version = "1.0-SNAPSHOT"
    repositories { mavenCentral() }
}

val bootVersion = "3.5.11"

subprojects {
    apply(plugin = "java-library")

    dependencies {
        add("implementation", platform   ("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
     //   add("implementation", platform ("org.liquibase:liquibase-core:4.25.1"))
      //  add("implementation", "org.springframework.boot:spring-boot-starter-liquibase:$bootVersion")


                add("testImplementation", platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
    }

  //  tasks.withType<Test>().configureEach { useJUnitPlatform() }
}

configure(subprojects.filter { it.name != "bootstrap" }) {
    apply(plugin = "java-library")
}