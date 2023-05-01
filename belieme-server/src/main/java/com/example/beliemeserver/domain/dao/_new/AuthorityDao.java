package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    @Transactional
    AuthorityDto update(UUID departmentId, Permission permission, AuthorityDto authority);
}
