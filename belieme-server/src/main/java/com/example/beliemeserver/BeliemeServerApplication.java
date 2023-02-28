package com.example.beliemeserver;

import com.example.beliemeserver.data.repository.custom.RefreshRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = RefreshRepositoryImpl.class)
public class BeliemeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeliemeServerApplication.class, args);
    }
}
