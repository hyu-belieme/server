package com.example.beliemeserver.preprocessing;

import com.example.beliemeserver.domain.service._new.NewDepartmentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class DepartmentInitializer implements CommandLineRunner {
    private final NewDepartmentService departmentService;

    public DepartmentInitializer(NewDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void run(String... args) {
        departmentService.initializeDepartments();
    }
}
