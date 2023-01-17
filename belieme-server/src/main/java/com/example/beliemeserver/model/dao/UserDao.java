package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDao {
    @Transactional
    List<UserDto> getAllList()
            throws DataException;

    @Transactional
    List<UserDto> getListByUniversity(String universityCode)
            throws NotFoundException, DataException;

    @Transactional
    UserDto getByToken(String token)
            throws NotFoundException, DataException;

    @Transactional
    UserDto getByIndex(String universityCode, String studentId)
            throws NotFoundException, DataException;

    @Transactional
    UserDto create(UserDto user)
            throws ConflictException, DataException, NotFoundException;

    @Transactional
    UserDto update(String universityCode, String studentId, UserDto newUser)
            throws NotFoundException, DataException, ConflictException;
}
