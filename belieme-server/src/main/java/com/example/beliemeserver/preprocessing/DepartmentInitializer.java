package com.example.beliemeserver.preprocessing;

import com.example.beliemeserver.domain.service.DepartmentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class DepartmentInitializer implements CommandLineRunner {
    private final DepartmentService departmentService;

    public DepartmentInitializer(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void run(String... args) {
        departmentService.initializeDepartments();
    }
}
