package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.OldUserEntity;
import com.example.beliemeserver.data.entity.id.OldUserId;
import com.example.beliemeserver.data.repository.old.OldUserRepository;
import com.example.beliemeserver.model.dao.old.UserDao;
import com.example.beliemeserver.model.dto.old.OldUserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class OldUserDaoImpl implements UserDao {
    @Autowired
    OldUserRepository userRepository;

    @Override
    public List<OldUserDto> getUsersData() throws DataException {
        Iterator<OldUserEntity> iterator = userRepository.findAll().iterator();

        ArrayList<OldUserDto> userDtoList = new ArrayList<>();
        while(iterator.hasNext()) {
            userDtoList.add(iterator.next().toUserDto());
        }
        return userDtoList;
    }

    @Override
    public OldUserDto getUserByTokenData(String token) throws NotFoundException, DataException {
        OldUserEntity userEntity = userRepository.findWithAuthoritiesByToken(token).orElse(null);
        if(userEntity == null) {
            throw new NotFoundException();
        }
        return userEntity.toUserDto();
    }

    @Override
    public OldUserDto getUserByStudentIdData(String studentId) throws NotFoundException, DataException {
        OldUserEntity userEntity = userRepository.findWithAuthoritiesByStudentId(studentId).orElse(null);
        if(userEntity == null) {
            throw new NotFoundException();
        }
        return userEntity.toUserDto();
    }

    @Override
    public OldUserDto addUserData(OldUserDto user) throws ConflictException, DataException {
        if(userRepository.existsById(new OldUserId(user.getStudentId()))) {
            throw new ConflictException();
        }

        OldUserEntity savedUser = userRepository.save(OldUserEntity.from(user));
        userRepository.refresh(savedUser);

        return savedUser.toUserDto();
    }

    @Override
    public OldUserDto updateUserData(String studentId, OldUserDto user) throws NotFoundException, DataException {
        OldUserEntity target = userRepository.findWithAuthoritiesByStudentId(studentId).orElse(null);
        OldUserEntity newUserEntity = OldUserEntity.from(user);
        if(target == null) {
            throw new NotFoundException();
        }
        target.setStudentId(newUserEntity.getStudentId());
        target.setName(newUserEntity.getName());
        target.setToken(newUserEntity.getToken());
        target.setCreateTimeStamp(newUserEntity.getCreateTimeStamp());
        target.setApprovalTimeStamp(newUserEntity.getApprovalTimeStamp());

        OldUserEntity savedUser = userRepository.save(target);
        userRepository.refresh(savedUser);

        return savedUser.toUserDto();
    }
}
