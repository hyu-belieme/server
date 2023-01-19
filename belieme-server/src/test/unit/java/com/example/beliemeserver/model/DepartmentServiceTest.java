package com.example.beliemeserver.model;

import com.example.beliemeserver.model.service.DepartmentService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest extends BaseServiceTest {
    @InjectMocks
    private DepartmentService departmentService;
}
