package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDao {
    @Transactional
    List<UserDto> getAllList();

    @Transactional
    List<UserDto> getListByUniversity(String universityCode);

    @Transactional
    UserDto getByToken(String token);

    @Transactional
    UserDto getByIndex(String universityCode, String studentId);

    @Transactional
    UserDto create(UserDto user);

    @Transactional
    UserDto update(String universityCode,
                   String studentId, UserDto newUser);
}
