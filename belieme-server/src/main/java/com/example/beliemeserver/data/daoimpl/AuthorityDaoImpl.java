package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.model.dao.AuthorityDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import org.springframework.stereotype.Component;

@Component
public class AuthorityDaoImpl implements AuthorityDao {
    @Override
    public AuthorityDto create(AuthorityDto authority) throws ConflictException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public AuthorityDto update(String universityCodeForUser, String studentId, String universityCodeForDepartment, String departmentCode) throws DataException {
        // TODO Need Implement
        return null;
    }
}
