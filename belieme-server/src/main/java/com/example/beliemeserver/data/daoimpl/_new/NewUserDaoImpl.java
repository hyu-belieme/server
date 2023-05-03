package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewAuthorityEntity;
import com.example.beliemeserver.data.entity._new.NewAuthorityUserJoinEntity;
import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.entity._new.NewUserEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.UserDao;
import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import com.example.beliemeserver.error.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewUserDaoImpl extends NewBaseDaoImpl implements UserDao {
    @Autowired
    public NewUserDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<UserDto> getAllList() {
        List<UserDto> output = new ArrayList<>();

        for (NewUserEntity userEntity : userRepository.findAll()) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public List<UserDto> getListByUniversity(String universityName) {
        List<UserDto> output = new ArrayList<>();

        UUID universityId = findUniversityEntity(universityName).getId();
        for (NewUserEntity userEntity : userRepository.findByUniversityId(universityId)) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public List<UserDto> getListByUniversity(UUID universityId) {
        List<UserDto> output = new ArrayList<>();

        for (NewUserEntity userEntity : userRepository.findByUniversityId(universityId)) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public UserDto getByToken(String token) {
        return findUserEntityByToken(token).toUserDto();
    }

    @Override
    public UserDto getById(UUID userId) {
        return findUserEntity(userId).toUserDto();
    }

    @Override
    public UserDto getByIndex(String universityName, String studentId) {
        return findUserEntity(universityName, studentId).toUserDto();
    }

    @Override
    public UserDto create(UserDto user) {
        NewUserEntity newUserEntity = saveUserOnly(user);
        newUserEntity = saveAuthorityJoins(newUserEntity, user.authorities());

        return newUserEntity.toUserDto();
    }

    @Override
    public UserDto update(String universityName, String studentId, UserDto newUser) {
        NewUserEntity target = findUserEntity(universityName, studentId);
        target = updateUserOnly(target, newUser);
        target = removeAllAuthorityJoins(target);
        target = saveAuthorityJoins(target, newUser.authorities());
        return userRepository.save(target).toUserDto();
    }

    @Override
    public UserDto update(UUID userId, UserDto newUser) {
        NewUserEntity target = findUserEntity(userId);
        target = updateUserOnly(target, newUser);
        target = removeAllAuthorityJoins(target);
        target = saveAuthorityJoins(target, newUser.authorities());
        return userRepository.save(target).toUserDto();
    }

    private NewUserEntity saveUserOnly(UserDto newUser) {
        NewUniversityEntity universityEntity = findUniversityEntity(newUser.university().id());

        checkUserConflict(universityEntity.getId(), newUser.studentId());

        NewUserEntity newUserEntity = new NewUserEntity(
                newUser.id(),
                universityEntity,
                newUser.studentId(),
                newUser.name(),
                newUser.entranceYear(),
                newUser.token(),
                newUser.createdAt(),
                newUser.approvedAt()
        );
        return userRepository.save(newUserEntity);
    }

    private NewUserEntity updateUserOnly(NewUserEntity target, UserDto newUser) {
        NewUniversityEntity newUniversity = findUniversityEntity(newUser.university().id());

        if (doesIndexChange(target, newUser)) {
            checkUserConflict(newUniversity.getId(), newUser.studentId());
        }

        return target
                .withUniversity(newUniversity)
                .withStudentId(newUser.studentId())
                .withName(newUser.name())
                .withEntranceYear(newUser.entranceYear())
                .withToken(newUser.token())
                .withCreatedAt(newUser.createdAt())
                .withApprovedAt(newUser.approvedAt());
    }

    private NewUserEntity saveAuthorityJoins(NewUserEntity newUserEntity, List<AuthorityDto> authorities) {
        for (AuthorityDto authority : authorities) {
            NewAuthorityEntity authorityEntity = findAuthorityEntity(
                    authority.department().id(), authority.permission().name());
            NewAuthorityUserJoinEntity newJoin = new NewAuthorityUserJoinEntity(
                    authorityEntity,
                    newUserEntity
            );
            NewAuthorityUserJoinEntity newAuthJoin = authorityUserJoinRepository.save(newJoin);
            newUserEntity = newUserEntity.withAuthorityAdd(newAuthJoin);
        }
        return newUserEntity;
    }

    private NewUserEntity removeAllAuthorityJoins(NewUserEntity user) {
        authorityUserJoinRepository.deleteAll(user.getAuthorityJoin());
        return user.withAuthorityClear();
    }

    private boolean doesIndexChange(NewUserEntity target, UserDto newUser) {
        String oldUniversityName = target.getUniversity().getName();
        String oldStudentId = target.getStudentId();

        return !(oldUniversityName.equals(newUser.university().name())
                && oldStudentId.equals(newUser.studentId()));
    }

    private void checkUserConflict(UUID universityId, String studentId) {
        if (userRepository.existsByUniversityIdAndStudentId(universityId, studentId)) {
            throw new ConflictException();
        }
    }
}
