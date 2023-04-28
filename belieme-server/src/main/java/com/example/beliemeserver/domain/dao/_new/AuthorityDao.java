package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorityDao {
    @Transactional
    List<AuthorityDto> getAllList();

    @Transactional
    boolean checkExistByIndex(String universityName, String departmentName, Permission permission);

    @Transactional
    AuthorityDto create(AuthorityDto authority);

    @Transactional
    AuthorityDto update(String universityName, String departmentName,
                        Permission permission, AuthorityDto authority);
}
