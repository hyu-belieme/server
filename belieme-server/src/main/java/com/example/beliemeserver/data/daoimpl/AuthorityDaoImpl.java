package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.model.dao.AuthorityDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorityDaoImpl implements AuthorityDao {
    @Override
    public List<AuthorityDto> getAllList() throws DataException {
        // TODO Need Implement
        return new ArrayList<>();
    }

    @Override
    public AuthorityDto create(AuthorityDto authority) throws ConflictException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public AuthorityDto update(String universityCodeForUser, String studentId, String universityCodeForDepartment, String departmentCode, AuthorityDto authority) throws DataException {
        // TODO Need Implement
        return null;
    }
}
