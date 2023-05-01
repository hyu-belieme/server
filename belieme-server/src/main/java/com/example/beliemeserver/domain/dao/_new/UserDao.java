package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserDao {
    @Transactional
    List<UserDto> getAllList();

    @Transactional
    List<UserDto> getListByUniversity(String universityName);

    @Transactional
    List<UserDto> getListByUniversity(UUID universityId);

    @Transactional
    UserDto getByToken(String token);

    @Transactional
    UserDto getById(UUID userId);

    @Transactional
    UserDto getByIndex(String universityName, String studentId);

    @Transactional
    UserDto create(UserDto user);

    @Transactional
    UserDto update(String universityName,
                   String studentId, UserDto newUser);

    @Transactional
    UserDto update(UUID userId, UserDto newUser);
}
