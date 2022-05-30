package com.example.beliemeserver.service;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundOnDBException;
import com.example.beliemeserver.model.dto.UserDto;

import java.util.List;

public interface UserService {
    public List<UserDto> getAllUser() throws FormatDoesNotMatchException;
    public UserDto getUserByToken(String token) throws FormatDoesNotMatchException, NotFoundOnDBException;
    public UserDto getUserByStudentId(String studentId) throws FormatDoesNotMatchException, NotFoundOnDBException;
    public UserDto addUser(UserDto user) throws FormatDoesNotMatchException;
    public UserDto updateUser(String studentId, UserDto user) throws NotFoundOnDBException, FormatDoesNotMatchException;
}
