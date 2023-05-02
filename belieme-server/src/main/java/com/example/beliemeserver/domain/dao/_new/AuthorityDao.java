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
    boolean checkExistByIndex(UUID departmentId, Permission permission);

    @Transactional
    AuthorityDto create(AuthorityDto authority);
}
