package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.data.entity.id.UserId;
import com.example.beliemeserver.data.repository.UserRepository;
import com.example.beliemeserver.model.dao.UserDao;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserDto> getUsersData() throws DataException {
        Iterator<UserEntity> iterator = userRepository.findAll().iterator();

        ArrayList<UserDto> userDtoList = new ArrayList<>();
        while(iterator.hasNext()) {
            userDtoList.add(iterator.next().toUserDto());
        }
        return userDtoList;
    }

    @Override
    public UserDto getUserByTokenData(String token) throws NotFoundException, DataException {
        UserEntity userEntity = userRepository.findWithAuthoritiesByToken(token).orElse(null);
        if(userEntity == null) {
            throw new NotFoundException();
        }
        return userEntity.toUserDto();
    }

    @Override
    public UserDto getUserByStudentIdData(String studentId) throws NotFoundException, DataException {
        UserEntity userEntity = userRepository.findWithAuthoritiesByStudentId(studentId).orElse(null);
        if(userEntity == null) {
            throw new NotFoundException();
        }
        return userEntity.toUserDto();
    }

    @Override
    public UserDto addUserData(UserDto user) throws ConflictException, DataException {
        if(userRepository.existsById(new UserId(user.getStudentId()))) {
            throw new ConflictException();
        }
        return userRepository.save(UserEntity.from(user)).toUserDto();
    }

    @Override
    public UserDto updateUserData(String studentId, UserDto user) throws NotFoundException, DataException {
        UserEntity target = userRepository.findWithAuthoritiesByStudentId(studentId).orElse(null);
        UserEntity newUserEntity = UserEntity.from(user);
        if(target == null) {
            throw new NotFoundException();
        }
        target.setStudentId(newUserEntity.getStudentId());
        target.setName(newUserEntity.getName());
        target.setToken(newUserEntity.getToken());
        target.setCreateTimeStamp(newUserEntity.getCreateTimeStamp());
        target.setApprovalTimeStamp(newUserEntity.getApprovalTimeStamp());
        return userRepository.save(target).toUserDto();
    }
}
