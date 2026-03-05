package ru.yp.marketapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = "ru.yp.marketapp")
//@EnableJpaRepositories(basePackages = "ru.yp.marketapp.adapters.persistence.jpa.repo")
//@EntityScan(basePackages = "ru.yp.marketapp.adapters.persistence.entity")

public class BootstrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootstrapApplication.class, args);
	}

}
