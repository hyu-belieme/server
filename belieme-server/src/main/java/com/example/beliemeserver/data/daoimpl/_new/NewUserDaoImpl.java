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
import lombok.NonNull;
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
    public List<UserDto> getListByUniversity(@NonNull UUID universityId) {
        List<UserDto> output = new ArrayList<>();

        for (NewUserEntity userEntity : userRepository.findByUniversityId(universityId)) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public UserDto getByToken(@NonNull String token) {
        return findUserEntityByToken(token).toUserDto();
    }

    @Override
    public UserDto getById(@NonNull UUID userId) {
        return findUserEntity(userId).toUserDto();
    }

    @Override
    public UserDto getByIndex(@NonNull UUID universityId, @NonNull String studentId) {
        return findUserEntity(universityId, studentId).toUserDto();
    }

    @Override
    public UserDto create(@NonNull UUID userId, @NonNull UUID universityId, @NonNull String studentId, @NonNull String name, int entranceYear, @NonNull String token, long createdAt, long approvedAt, @NonNull List<AuthorityDto> authorities) {
        NewUserEntity newUserEntity = saveUserOnly(userId, universityId, studentId, name, entranceYear, token, createdAt, approvedAt);
        newUserEntity = saveAuthorityJoins(newUserEntity, authorities);

        return newUserEntity.toUserDto();
    }

    @Override
    public UserDto update(@NonNull UUID userId, @NonNull UUID universityId, @NonNull String studentId, @NonNull String name, int entranceYear, @NonNull String token, long createdAt, long approvedAt, @NonNull List<AuthorityDto> authorities) {
        NewUserEntity target = findUserEntity(userId);
        target = updateUserOnly(target, universityId, studentId, name, entranceYear, token, createdAt, approvedAt);
        target = removeAllAuthorityJoins(target);
        target = saveAuthorityJoins(target, authorities);
        return userRepository.save(target).toUserDto();
    }

    private NewUserEntity saveUserOnly(UUID userId, UUID universityId, String studentId, String name, int entranceYear, String token, long createdAt, long approvedAt) {
        NewUniversityEntity universityEntity = findUniversityEntity(universityId);

        checkUserIdConflict(userId);
        checkUserConflict(universityEntity.getId(), studentId);

        NewUserEntity newUserEntity = new NewUserEntity(
                userId,
                universityEntity,
                studentId,
                name,
                entranceYear,
                token,
                createdAt,
                approvedAt
        );
        return userRepository.save(newUserEntity);
    }

    private NewUserEntity updateUserOnly(NewUserEntity target, UUID universityId, String studentId, String name, int entranceYear, String token, long createdAt, long approvedAt) {
        NewUniversityEntity newUniversity = findUniversityEntity(universityId);

        if (doesIndexChange(target, universityId, studentId)) {
            checkUserConflict(newUniversity.getId(), studentId);
        }

        return target
                .withUniversity(newUniversity)
                .withStudentId(studentId)
                .withName(name)
                .withEntranceYear(entranceYear)
                .withToken(token)
                .withCreatedAt(createdAt)
                .withApprovedAt(approvedAt);
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

    private boolean doesIndexChange(NewUserEntity target, UUID newUnivId, String newStudentId) {
        UUID oldUniversityId = target.getUniversity().getId();
        String oldStudentId = target.getStudentId();

        return !(oldUniversityId.equals(newUnivId)
                && oldStudentId.equals(newStudentId));
    }

    private void checkUserIdConflict(UUID userId) {
        if (userRepository.existsById(userId)) {
            throw new ConflictException();
        }
    }

    private void checkUserConflict(UUID universityId, String studentId) {
        if (userRepository.existsByUniversityIdAndStudentId(universityId, studentId)) {
            throw new ConflictException();
        }
    }
}
