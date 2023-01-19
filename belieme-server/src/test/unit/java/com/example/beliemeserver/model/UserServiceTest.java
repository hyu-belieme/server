package com.example.beliemeserver.model;

import com.example.beliemeserver.model.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends BaseServiceTest {
    @InjectMocks
    private UserService userService;
}
