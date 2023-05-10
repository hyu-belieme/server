package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.entity.AuthorityUserJoinEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.domain.dao.UserDao;
import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    @Autowired
    public UserDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<UserDto> getAllList() {
        List<UserDto> output = new ArrayList<>();

        for (UserEntity userEntity : userRepository.findAll()) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public List<UserDto> getListByUniversity(@NonNull UUID universityId) {
        List<UserDto> output = new ArrayList<>();

        validateUniversityId(universityId);
        for (UserEntity userEntity : userRepository.findByUniversityId(universityId)) {
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
        UserEntity newUserEntity = saveUserOnly(userId, universityId, studentId, name, entranceYear, token, createdAt, approvedAt);
        newUserEntity = saveAuthorityJoins(newUserEntity, authorities);

        return newUserEntity.toUserDto();
    }

    @Override
    public UserDto update(@NonNull UUID userId, @NonNull UUID universityId, @NonNull String studentId, @NonNull String name, int entranceYear, @NonNull String token, long createdAt, long approvedAt, @NonNull List<AuthorityDto> authorities) {
        UserEntity target = findUserEntity(userId);
        target = updateUserOnly(target, universityId, studentId, name, entranceYear, token, createdAt, approvedAt);
        target = removeAllAuthorityJoins(target);
        target = saveAuthorityJoins(target, authorities);
        return userRepository.save(target).toUserDto();
    }

    private UserEntity saveUserOnly(UUID userId, UUID universityId, String studentId, String name, int entranceYear, String token, long createdAt, long approvedAt) {
        UniversityEntity universityEntity = getUniversityEntityOrThrowInvalidIndexException(universityId);

        checkUserIdConflict(userId);
        checkUserConflict(universityEntity.getId(), studentId);

        UserEntity newUserEntity = new UserEntity(
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

    private UserEntity updateUserOnly(UserEntity target, UUID universityId, String studentId, String name, int entranceYear, String token, long createdAt, long approvedAt) {
        UniversityEntity newUniversity = getUniversityEntityOrThrowInvalidIndexException(universityId);

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

    private UserEntity saveAuthorityJoins(UserEntity newUserEntity, List<AuthorityDto> authorities) {
        for (AuthorityDto authority : authorities) {
            AuthorityEntity authorityEntity = findAuthorityEntity(
                    authority.department().id(), authority.permission().name());
            AuthorityUserJoinEntity newJoin = new AuthorityUserJoinEntity(
                    authorityEntity,
                    newUserEntity
            );
            AuthorityUserJoinEntity newAuthJoin = authorityUserJoinRepository.save(newJoin);
            newUserEntity = newUserEntity.withAuthorityAdd(newAuthJoin);
        }
        return newUserEntity;
    }

    private UserEntity removeAllAuthorityJoins(UserEntity user) {
        authorityUserJoinRepository.deleteAll(user.getAuthorityJoin());
        return user.withAuthorityClear();
    }

    private boolean doesIndexChange(UserEntity target, UUID newUnivId, String newStudentId) {
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
