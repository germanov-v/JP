package ru.yp.marketapp.adapters;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "ru.yp.marketapp.adapters.persistence")
public class PersistenceTestApplication {
}
