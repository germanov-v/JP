plugins {
  //  id("java") - НЕ МОДУЛЬ, а root (!)
  //  id("io.spring.dependency-management")  version "1.1.7" apply false
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
// gradle build
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")
    repositories { mavenCentral() }


//    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
//        imports {
//            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.11")
//        }
//    }

        // BOM
    dependencies {
        add("implementation", platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
        add("testImplementation", platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))

//        implementation(platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
//        testImplementation(platform("org.springframework.boot:spring-boot-dependencies:$bootVersion"))
//
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

//dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//}
//
//tasks.test {
//    useJUnitPlatform()
//}