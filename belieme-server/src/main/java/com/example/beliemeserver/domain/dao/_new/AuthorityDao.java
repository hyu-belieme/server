package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
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
