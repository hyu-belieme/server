package com.example.beliemeserver.model;

import com.example.beliemeserver.model.dao.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {
    @Mock
    protected UniversityDao universityDao;
    @Mock
    protected DepartmentDao departmentDao;
    @Mock
    protected UserDao userDao;
    @Mock
    protected MajorDao majorDao;
    @Mock
    protected AuthorityDao authorityDao;
    @Mock
    protected StuffDao stuffDao;
    @Mock
    protected ItemDao itemDao;
    @Mock
    protected HistoryDao historyDao;

    public static final String userToken = "";
}
