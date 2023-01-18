package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import org.springframework.stereotype.Service;

@Service
public class UniversityService extends BaseService {
    public UniversityService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }
}
