package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorityDao {
    @Transactional
    List<AuthorityDto> getAllList() throws FormatDoesNotMatchException;

    @Transactional
    AuthorityDto create(AuthorityDto authority)
            throws ConflictException, NotFoundException, FormatDoesNotMatchException;

    @Transactional
    AuthorityDto update(String universityCode, String departmentCode,
                        AuthorityDto.Permission permission, AuthorityDto authority)
            throws NotFoundException, ConflictException, FormatDoesNotMatchException;
}
