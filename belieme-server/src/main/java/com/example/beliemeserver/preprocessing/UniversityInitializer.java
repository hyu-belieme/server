package com.example.beliemeserver.preprocessing;

import com.example.beliemeserver.model.service.UniversityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class UniversityInitializer implements CommandLineRunner {
    private final UniversityService universityService;

    public UniversityInitializer(UniversityService universityService) {
        this.universityService = universityService;
    }

    @Override
    public void run(String... args) {
        universityService.initializeUniversities();
    }
}
