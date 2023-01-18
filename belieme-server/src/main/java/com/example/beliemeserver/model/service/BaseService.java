package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseService {
    private final UniversityDao universityDao;
    private final DepartmentDao departmentDao;
    private final UserDao userDao;
    private final MajorDao majorDao;
    private final AuthorityDao authorityDao;
    private final StuffDao stuffDao;
    private final ItemDao itemDao;
    private final HistoryDao historyDao;

    public BaseService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        this.universityDao = universityDao;
        this.departmentDao = departmentDao;
        this.userDao = userDao;
        this.majorDao = majorDao;
        this.authorityDao = authorityDao;
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.historyDao = historyDao;
    }
}
