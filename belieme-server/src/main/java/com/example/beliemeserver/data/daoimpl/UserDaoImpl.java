package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.*;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.UserDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    @Autowired
    public UserDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<UserDto> getAllList() throws FormatDoesNotMatchException {
        List<UserDto> output = new ArrayList<>();

        for(UserEntity userEntity : userRepository.findAll()) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public List<UserDto> getListByUniversity(String universityCode) throws NotFoundException, FormatDoesNotMatchException {
        List<UserDto> output = new ArrayList<>();

        int universityId = getUniversityEntity(universityCode).getId();
        for(UserEntity userEntity : userRepository.findByUniversityId(universityId)) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    public UserDto getByToken(String token) throws NotFoundException, FormatDoesNotMatchException {
        return getUserEntityByToken(token).toUserDto();
    }

    @Override
    public UserDto getByIndex(String universityCode, String studentId) throws NotFoundException, FormatDoesNotMatchException {
        return getUserEntity(universityCode, studentId).toUserDto();
    }

    @Override
    public UserDto create(UserDto user) throws ConflictException, NotFoundException, FormatDoesNotMatchException {
        UserEntity newUserEntity = saveUserOnly(user);
        saveMajorJoins(newUserEntity, user.majors());
        saveAuthorityJoins(newUserEntity, user.authorities());

        return newUserEntity.toUserDto();
    }

    @Override
    public UserDto update(String universityCode, String studentId, UserDto newUser) throws NotFoundException, ConflictException, FormatDoesNotMatchException {
        UserEntity target = getUserEntity(universityCode, studentId);
        updateUserOnly(target, newUser);

        removeAllMajorJoins(target);
        removeAllAuthorityJoins(target);

        saveMajorJoins(target, newUser.majors());
        saveAuthorityJoins(target, newUser.authorities());
        return target.toUserDto();
    }

    private UserEntity saveUserOnly(UserDto newUser) throws NotFoundException, ConflictException {
        UniversityEntity universityEntity = getUniversityEntity(newUser.university());

        checkUserConflict(universityEntity.getId(), newUser.studentId());

        UserEntity newUserEntity = new UserEntity(
                universityEntity,
                newUser.studentId(),
                newUser.name(),
                newUser.token(),
                newUser.createTimeStamp(),
                newUser.approvalTimeStamp()
        );
        return userRepository.save(newUserEntity);
    }

    private void updateUserOnly(UserEntity target, UserDto newUser) throws NotFoundException, ConflictException {
        UniversityEntity newUniversity = getUniversityEntity(newUser.university());

        if(doesIndexChange(target, newUser)) {
            checkUserConflict(newUniversity.getId(), newUser.studentId());
        }

        target.setUniversity(newUniversity)
                .setStudentId(newUser.studentId())
                .setName(newUser.name())
                .setToken(newUser.token())
                .setCreateTimeStamp(newUser.createTimeStamp())
                .setApprovalTimeStamp(newUser.approvalTimeStamp());
    }

    private void saveMajorJoins(UserEntity newUserEntity, List<MajorDto> majors) throws NotFoundException {
        for(MajorDto major: majors) {
            MajorEntity baseMajorEntity = getMajorEntity(major);
            MajorUserJoinEntity newJoin = new MajorUserJoinEntity(
                    baseMajorEntity,
                    newUserEntity
            );
            majorUserJoinRepository.save(newJoin);
        }
    }

    private void saveAuthorityJoins(UserEntity newUserEntity, List<AuthorityDto> authorities) throws NotFoundException {
        for(AuthorityDto authority: authorities) {
            AuthorityEntity authorityEntity = getAuthorityEntity(authority);
            AuthorityUserJoinEntity newJoin = new AuthorityUserJoinEntity(
                    authorityEntity,
                    newUserEntity
            );
            authorityUserJoinRepository.save(newJoin);
        }
    }

    private void removeAllMajorJoins(UserEntity user) {
        while (user.getMajorJoin().size() > 0) {
            majorUserJoinRepository.delete(user.getMajorJoin().get(0));
        }
    }

    private void removeAllAuthorityJoins(UserEntity user) {
        while (user.getAuthorityJoin().size() > 0) {
            authorityUserJoinRepository.delete(user.getAuthorityJoin().get(0));
        }
    }

    private boolean doesIndexChange(UserEntity target, UserDto newUser) {
        String oldUniversityCode = target.getUniversity().getCode();
        String oldStudentId = target.getStudentId();

        return !(oldUniversityCode.equals(newUser.university().code())
                && oldStudentId.equals(newUser.studentId()));
    }

    private void checkUserConflict(int universityId, String studentId) throws ConflictException {
        if(userRepository.existsByUniversityIdAndStudentId(universityId, studentId)) {
            throw new ConflictException();
        }
    }
}
