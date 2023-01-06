package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.model.dao.UserDao;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    @Override
    public List<UserDto> getAllList() throws DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public List<UserDto> getListByUniversity(String universityCode) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public UserDto getByToken(String token) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public UserDto getByIndex(String universityCode, String studentId) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public UserDto create(UserDto user) throws ConflictException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public UserDto update(String universityCode, String studentId, UserDto newUser) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }
}
