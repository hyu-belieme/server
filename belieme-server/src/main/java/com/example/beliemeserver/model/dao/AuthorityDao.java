package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;

public interface AuthorityDao {
    AuthorityDto create(AuthorityDto authority)
            throws ConflictException, DataException;

    AuthorityDto update(
            String universityCodeForUser, String studentId,
            String universityCodeForDepartment, String departmentCode)
            throws DataException;
}
