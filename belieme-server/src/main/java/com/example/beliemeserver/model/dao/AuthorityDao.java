package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorityDao {
    @Transactional
    List<AuthorityDto> getAllList() throws DataException;

    @Transactional
    AuthorityDto create(AuthorityDto authority)
            throws ConflictException, DataException, NotFoundException;

    @Transactional
    AuthorityDto update(String universityCode, String departmentCode,
                        AuthorityDto.Permission permission, AuthorityDto authority)
            throws DataException, NotFoundException, ConflictException;
}
