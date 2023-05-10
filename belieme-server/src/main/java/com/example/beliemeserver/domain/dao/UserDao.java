package com.example.beliemeserver.domain.dao;

import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.UserDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserDao {
    @Transactional
    List<UserDto> getAllList();

    @Transactional
    List<UserDto> getListByUniversity(@NonNull UUID universityId);

    @Transactional
    UserDto getByToken(@NonNull String token);

    @Transactional
    UserDto getById(@NonNull UUID userId);

    @Transactional
    UserDto getByIndex(@NonNull UUID universityId, @NonNull String studentId);

    @Transactional
    UserDto create(
            @NonNull UUID userId,
            @NonNull UUID universityId,
            @NonNull String studentId,
            @NonNull String name,
            int entranceYear,
            @NonNull String token,
            long createdAt,
            long approvedAt,
            @NonNull List<AuthorityDto> authorities);

    @Transactional
    UserDto update(
            @NonNull UUID userId,
            @NonNull UUID universityId,
            @NonNull String studentId,
            @NonNull String name,
            int entranceYear,
            @NonNull String token,
            long createdAt,
            long approvedAt,
            @NonNull List<AuthorityDto> authorities);
}
