package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorityDao {
    @Transactional
    List<AuthorityDto> getAllList();

    @Transactional
    boolean checkExistByIndex(String universityCode, String departmentCode, Permission permission);

    @Transactional
    AuthorityDto create(AuthorityDto authority);

    @Transactional
    AuthorityDto update(String universityCode, String departmentCode,
                        Permission permission, AuthorityDto authority);
}
