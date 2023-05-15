package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface AuthorityDao {
    @Transactional
    List<AuthorityDto> getAllList();

    @Transactional
    boolean checkExistByIndex(@NonNull UUID departmentId, @NonNull Permission permission);

    @Transactional
    AuthorityDto create(@NonNull UUID departmentId, @NonNull Permission permission);
}
