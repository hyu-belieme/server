package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.*;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.UserDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    @Autowired
    public UserDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    @Transactional
    public List<UserDto> getAllList() throws DataException {
        List<UserDto> output = new ArrayList<>();

        for(UserEntity userEntity : userRepository.findAll()) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    @Transactional
    public List<UserDto> getListByUniversity(String universityCode) throws NotFoundException, DataException {
        List<UserDto> output = new ArrayList<>();

        int universityId = getUniversityEntity(universityCode).getId();
        for(UserEntity userEntity : userRepository.findByUniversityId(universityId)) {
            output.add(userEntity.toUserDto());
        }
        return output;
    }

    @Override
    @Transactional
    public UserDto getByToken(String token) throws NotFoundException, DataException {
        return getUserEntityByToken(token).toUserDto();
    }

    @Override
    @Transactional
    public UserDto getByIndex(String universityCode, String studentId) throws NotFoundException, DataException {
        return getUserEntity(universityCode, studentId).toUserDto();
    }

    @Override
    @Transactional
    public UserDto create(UserDto user) throws ConflictException, DataException, NotFoundException {
        UserEntity newUserEntity = saveUserOnly(user);
        saveMajorJoins(newUserEntity, user.majors());
        saveAuthorityJoins(newUserEntity, user.authorities());

        return newUserEntity.toUserDto();
    }

    @Override
    @Transactional
    public UserDto update(String universityCode, String studentId, UserDto newUser) throws NotFoundException, DataException, ConflictException {
        UserEntity target = getUserEntity(universityCode, studentId);
        updateUserOnly(target, newUser);

        removeAllMajorJoins(target);
        removeAllAuthorityJoins(target);

        saveMajorJoins(target, newUser.majors());
        saveAuthorityJoins(target, newUser.authorities());
        return null;
    }

    private void checkUserConflict(int universityId, String studentId) throws ConflictException {
        if(userRepository.existsByUniversityIdAndStudentId(universityId, studentId)) {
            throw new ConflictException();
        }
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
        UniversityEntity newUniversity = getUniversityEntity(newUser.university().code());

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
        String newUniversityCode = newUser.university().code();
        String newStudentId = newUser.studentId();

        return !(oldUniversityCode.equals(newUniversityCode) && oldStudentId.equals(newStudentId));
    }
}
