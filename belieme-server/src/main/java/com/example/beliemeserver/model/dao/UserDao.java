package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDao {
    @Transactional
    List<UserDto> getAllList() throws FormatDoesNotMatchException;

    @Transactional
    List<UserDto> getListByUniversity(String universityCode)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    UserDto getByToken(String token)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    UserDto getByIndex(String universityCode, String studentId)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    UserDto create(UserDto user)
            throws ConflictException, NotFoundException, FormatDoesNotMatchException;

    @Transactional
    UserDto update(String universityCode, String studentId, UserDto newUser)
            throws NotFoundException, ConflictException, FormatDoesNotMatchException;
}
