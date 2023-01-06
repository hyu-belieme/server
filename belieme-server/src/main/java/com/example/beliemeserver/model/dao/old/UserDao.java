package com.example.beliemeserver.model.dao.old;

import com.example.beliemeserver.model.dto.old.OldUserDto;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface UserDao {
    public List<OldUserDto> getUsersData() throws DataException;
    public OldUserDto getUserByTokenData(String token) throws NotFoundException, DataException;
    public OldUserDto getUserByStudentIdData(String studentId) throws NotFoundException, DataException;
    public OldUserDto addUserData(OldUserDto user) throws ConflictException, DataException;
    public OldUserDto updateUserData(String studentId, OldUserDto user) throws NotFoundException, DataException;
}
