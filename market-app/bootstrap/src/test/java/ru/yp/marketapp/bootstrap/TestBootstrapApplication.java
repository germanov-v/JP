package ru.yp.marketapp.bootstrap;

import org.springframework.boot.SpringApplication;

public class TestBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.from(BootstrapApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
