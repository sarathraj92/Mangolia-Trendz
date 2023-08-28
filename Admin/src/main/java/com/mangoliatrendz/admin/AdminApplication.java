package com.mangoliatrendz.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.mangoliatrendz.library.*", "com.mangoliatrendz.admin.*"})
@EnableJpaRepositories(value = "com.mangoliatrendz.library.repository")
@EntityScan(value = "com.mangoliatrendz.library.model")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
