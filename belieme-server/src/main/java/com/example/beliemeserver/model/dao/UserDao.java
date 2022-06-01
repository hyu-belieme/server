package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface UserDao {
    public List<UserDto> getUsersData() throws DataException;
    public UserDto getUserByTokenData(String token) throws NotFoundException, DataException;
    public UserDto getUserByStudentIdData(String studentId) throws NotFoundException, DataException;
    public UserDto addUserData(UserDto user) throws ConflictException, DataException;
    public UserDto updateUserData(String studentId, UserDto user) throws NotFoundException, DataException;
}
