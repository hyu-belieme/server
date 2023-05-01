package com.example.beliemeserver.preprocessing;

import com.example.beliemeserver.domain.service._new.NewUniversityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class UniversityInitializer implements CommandLineRunner {
    private final NewUniversityService universityService;

    public UniversityInitializer(NewUniversityService universityService) {
        this.universityService = universityService;
    }

    @Override
    public void run(String... args) {
        universityService.initializeUniversities();
    }
}
