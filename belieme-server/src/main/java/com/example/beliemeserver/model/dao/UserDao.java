package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface UserDao {
    List<UserDto> getAllList()
            throws DataException;

    List<UserDto> getListByUniversity(String universityCode)
            throws NotFoundException, DataException;

    UserDto getByToken(String token)
            throws NotFoundException, DataException;

    UserDto getByIndex(String universityCode, String studentId)
            throws NotFoundException, DataException;

    UserDto create(UserDto user)
            throws ConflictException, DataException, NotFoundException;

    UserDto update(String universityCode, String studentId, UserDto newUser)
            throws NotFoundException, DataException, ConflictException;
}
