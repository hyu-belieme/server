package com.example.beliemeserver.data;

import com.example.beliemeserver.data.daoimpl._new.NewUserDaoImpl;
import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.domain.dto._new.UserDto;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.util.RandomGetter;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewUserDaoTest  extends BaseDaoTest {
    @InjectMocks
    private NewUserDaoImpl userDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<UserDto> execMethod() {
            return userDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(userRepository.findAll()).thenReturn(stub.ALL_USERS);
            TestHelper.listCompareTest(this::execMethod, toUserDtoList(stub.ALL_USERS));
        }
    }

    @Nested
    @DisplayName("getListByUniversity()")
    public class TestGetListByUniversity {
        private NewUniversityEntity univ;
        private UUID univId;

        private void setUniv(NewUniversityEntity univ) {
            this.univ = univ;
            this.univId = univ.getId();
        }

        protected List<UserDto> execMethod() {
            return userDao.getListByUniversity(univId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUniv(randomUniv());

            List<NewUserEntity> targets = new ArrayList<>();
            for(NewUserEntity user : stub.ALL_USERS) {
                if(user.getUniversity().getId().equals(univId)) targets.add(user);
            }

            when(univRepository.existsById(univId)).thenReturn(true);
            when(userRepository.findByUniversityId(univId)).thenReturn(targets);
            TestHelper.listCompareTest(this::execMethod, toUserDtoList(targets));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[잘못된 `universityId`가 주어졌을 시]_[InvalidIndexException]")
        public void ERROR_wrongUniversityId_invalidIndexException() {
            setUniv(randomUniv());

            when(univRepository.existsById(univId)).thenReturn(false);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById {
        private NewUserEntity user;
        private UUID userId;

        private void setUser(NewUserEntity user) {
            this.user = user;
            this.userId = user.getId();
        }

        protected UserDto execMethod() {
            return userDao.getById(userId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUser(randomUser());

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            TestHelper.objectCompareTest(this::execMethod, user.toUserDto());
        }

        @Test
        @DisplayName("[ERROR]_[`userId`에 해당하는 `user`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setUser(randomUser());

            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex {
        private NewUserEntity user;
        private UUID univId;
        private String studentId;

        private void setUser(NewUserEntity user) {
            this.user = user;
            this.univId = user.getUniversityId();
            this.studentId = user.getStudentId();
        }

        protected UserDto execMethod() {
            return userDao.getByIndex(univId, studentId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUser(randomUser());

            when(userRepository.findByUniversityIdAndStudentId(univId, studentId)).thenReturn(Optional.of(user));
            TestHelper.objectCompareTest(this::execMethod, user.toUserDto());
        }

        @Test
        @DisplayName("[ERROR]_[`index`에 해당하는 `user`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setUser(randomUser());

            when(userRepository.findByUniversityIdAndStudentId(univId, studentId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByToken()")
    public class TestGetByToken {
        private NewUserEntity user;
        private final String token = UUID.randomUUID().toString();

        private void setUser(NewUserEntity user) {
            this.user = user.withToken(token);
        }

        protected UserDto execMethod() {
            return userDao.getByToken(token);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUser(randomUser());

            when(userRepository.findByToken(token)).thenReturn(Optional.of(user));
            TestHelper.objectCompareTest(this::execMethod, user.toUserDto());
        }

        @Test
        @DisplayName("[ERROR]_[`token`에 해당하는 `user`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setUser(randomUser());

            when(userRepository.findByToken(token)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private NewUserEntity user;

        private void setUser(NewUserEntity user) {
            this.user = user;
        }

        protected UserDto execMethod() {
            return userDao.create(
                    user.getId(),
                    user.getUniversityId(),
                    user.getStudentId(),
                    user.getName(),
                    user.getEntranceYear(),
                    user.getToken(),
                    user.getCreatedAt(),
                    user.getApprovedAt(),
                    user.getAuthorityJoin().stream().map(e -> e.getAuthority().toAuthorityDto()).toList()
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUser(randomUser());

            when(univRepository.findById(user.getUniversityId())).thenReturn(Optional.of(user.getUniversity()));
            when(userRepository.existsById(user.getId())).thenReturn(false);
            when(userRepository.existsByUniversityIdAndStudentId(user.getUniversityId(), user.getStudentId())).thenReturn(false);
            for(NewAuthorityUserJoinEntity authJoin : user.getAuthorityJoin()) {
                when(authUserJoinRepository.save(mockArg(authJoin))).thenReturn(authJoin);
            }
            when(userRepository.save(mockArg(user))).thenReturn(user);

            TestHelper.objectCompareTest(this::execMethod, user.toUserDto());

            for(NewAuthorityUserJoinEntity authJoin : user.getAuthorityJoin()) {
                verify(authUserJoinRepository).save(mockArg(authJoin));
            }
            verify(userRepository).save(mockArg(user));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `id`를 갖는 `user`가 존재할 시]_[ConflictException]")
        public void ERROR_idConflict_ConflictException() {
            setUser(randomUser());

            when(univRepository.findById(user.getUniversityId())).thenReturn(Optional.of(user.getUniversity()));
            when(userRepository.existsById(user.getId())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `user`가 존재할 시]_[ConflictException]")
        public void ERROR_indexConflict_ConflictException() {
            setUser(randomUser());

            when(univRepository.findById(user.getUniversityId())).thenReturn(Optional.of(user.getUniversity()));
            when(userRepository.existsById(user.getId())).thenReturn(false);
            when(userRepository.existsByUniversityIdAndStudentId(user.getUniversityId(), user.getStudentId())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`index`에 해당하는 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_universityNotExist_InvalidIndexException() {
            setUser(randomUser());

            when(univRepository.findById(user.getUniversityId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        private NewUserEntity target;
        private UUID targetId;
        private NewUserEntity newUser;

        private void setTarget(NewUserEntity dept) {
            this.target = dept;
            this.targetId = dept.getId();
        }

        private NewUserEntity savedUser() {
            return target
                    .withUniversity(newUser.getUniversity())
                    .withStudentId(newUser.getStudentId())
                    .withName(newUser.getName())
                    .withEntranceYear(newUser.getEntranceYear())
                    .withToken(newUser.getToken())
                    .withCreatedAt(newUser.getCreatedAt())
                    .withApprovedAt(newUser.getApprovedAt())
                    .withAuthorityJoin(newUser.getAuthorityJoin());
        }

        protected UserDto execMethod() {
            return userDao.update(
                    targetId,
                    newUser.getUniversityId(),
                    newUser.getStudentId(),
                    newUser.getName(),
                    newUser.getEntranceYear(),
                    newUser.getToken(),
                    newUser.getCreatedAt(),
                    newUser.getApprovedAt(),
                    newUser.getAuthorityJoin().stream().map(e -> e.getAuthority().toAuthorityDto()).toList()
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`index`을 변경하지 않을 시]_[-]")
        public void SUCCESS_notChangeIndex() {
            setTarget(randomUser());

            newUser = target
                    .withName("__NEW_NAME")
                    .withToken(UUID.randomUUID().toString())
                    .withApprovedAt(1683076516);
            newUser = addNewAuth(newUser);
            newUser = addNewAuth(newUser);

            when(userRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newUser.getUniversityId())).thenReturn(Optional.of(newUser.getUniversity()));

            for(NewAuthorityUserJoinEntity authJoin : newUser.getAuthorityJoin()) {
                when(authRepository.findByDepartmentIdAndPermission(authJoin.getAuthority().getDepartmentId(), authJoin.getAuthority().getPermission()))
                        .thenReturn(Optional.of(authJoin.getAuthority()));
                when(authUserJoinRepository.save(mockArg(authJoin))).thenReturn(authJoin);
            }
            when(userRepository.save(mockArg(savedUser()))).thenReturn(savedUser());

            TestHelper.objectCompareTest(this::execMethod, savedUser().toUserDto());

            for(NewAuthorityUserJoinEntity authJoin : newUser.getAuthorityJoin()) {
                verify(authUserJoinRepository).save(mockArg(authJoin));
            }
            verify(userRepository).save(mockArg(savedUser()));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`index`를 변경할 시]_[-]")
        public void SUCCESS_changeIndex() {
            setTarget(randomUser());

            newUser = target
                    .withUniversity(randomUniv())
                    .withStudentId("__NEW_ID")
                    .withEntranceYear(9870);
            newUser = addNewAuth(newUser);
            newUser = addNewAuth(newUser);

            when(userRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newUser.getUniversityId())).thenReturn(Optional.of(newUser.getUniversity()));
            when(userRepository.existsByUniversityIdAndStudentId(newUser.getUniversityId(), newUser.getStudentId())).thenReturn(false);

            for(NewAuthorityUserJoinEntity authJoin : newUser.getAuthorityJoin()) {
                when(authRepository.findByDepartmentIdAndPermission(authJoin.getAuthority().getDepartmentId(), authJoin.getAuthority().getPermission()))
                        .thenReturn(Optional.of(authJoin.getAuthority()));
                when(authUserJoinRepository.save(mockArg(authJoin))).thenReturn(authJoin);
            }
            when(userRepository.save(mockArg(savedUser()))).thenReturn(savedUser());

            TestHelper.objectCompareTest(this::execMethod, savedUser().toUserDto());

            for(NewAuthorityUserJoinEntity authJoin : newUser.getAuthorityJoin()) {
                verify(authUserJoinRepository).save(mockArg(authJoin));
            }
            verify(userRepository).save(mockArg(savedUser()));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`id`에 해당하는 `user`가 없을 시]_[NotFoundException]")
        public void ERROR_targetNotFound_NotFoundException() {
            setTarget(randomUser());

            newUser = target
                    .withUniversity(randomUniv())
                    .withStudentId("__NEW_ID")
                    .withEntranceYear(9870);
            newUser = addNewAuth(newUser);
            newUser = addNewAuth(newUser);

            when(userRepository.findById(targetId)).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`NewUser`의 `index`에 해당하는 `university`가 없을 시]_[InvalidIndexException]")
        public void ERROR_univOfNewDeptNotFound_InvalidIndexException() {
            setTarget(randomUser());

            newUser = target
                    .withUniversity(randomUniv())
                    .withStudentId("__NEW_ID")
                    .withEntranceYear(9870);
            newUser = addNewAuth(newUser);
            newUser = addNewAuth(newUser);

            when(userRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newUser.getUniversityId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`NewUser`의 `index`에 해당하는 `user`가 기존에 있을 시]_[ConflictException]")
        public void ERROR_newDeptConflict_ConflictException() {
            setTarget(randomUser());

            newUser = target
                    .withUniversity(randomUniv())
                    .withStudentId("__NEW_ID")
                    .withEntranceYear(9870);
            newUser = addNewAuth(newUser);
            newUser = addNewAuth(newUser);

            when(userRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newUser.getUniversityId())).thenReturn(Optional.of(newUser.getUniversity()));
            when(userRepository.existsByUniversityIdAndStudentId(newUser.getUniversityId(), newUser.getStudentId())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        private NewUserEntity addNewAuth(NewUserEntity user) {
            NewAuthorityEntity newAuth = randomAuthorityNotInUser(user);
            if(newAuth == null) return user;
            return user.withAuthorityAdd(new NewAuthorityUserJoinEntity(newAuth, user));
        }
    }

    private NewAuthorityEntity randomAuthorityNotInUser(NewUserEntity user) {
        return randomSelectAndLog(authoritiesNotInUser(allAuths(), user));
    }

    private RandomGetter<NewAuthorityEntity> authoritiesNotInUser(RandomGetter<NewAuthorityEntity> auths, NewUserEntity user) {
        return auths.filter((auth) -> !doesDeptContainAuth(user, auth));
    }

    private boolean doesDeptContainAuth(NewUserEntity user, NewAuthorityEntity auth) {
        for(NewAuthorityUserJoinEntity authJoin : user.getAuthorityJoin()) {
            if(authJoin.getAuthority().equals(auth)) return true;
        }
        return false;
    }
}
