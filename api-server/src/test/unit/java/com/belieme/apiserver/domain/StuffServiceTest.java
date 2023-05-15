package com.belieme.apiserver.domain;

import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.domain.exception.ItemAmountLimitExceededException;
import com.belieme.apiserver.domain.exception.PermissionDeniedException;
import com.belieme.apiserver.domain.service.StuffService;
import com.belieme.apiserver.error.exception.ConflictException;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StuffServiceTest extends BaseServiceTest {
    private final DepartmentDto TEST_DEPT = stub.HYU_CSE_DEPT;

    @InjectMocks
    private StuffService stuffService;

    @Nested
    @DisplayName("getListByDepartment()")
    public final class TestGetListByDepartment extends StuffNestedTest {
        private List<StuffDto> stuffList;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(
                    dept, Permission.USER));

            stuffList = getStuffListByDept(dept);
        }

        @Override
        protected List<StuffDto> execMethod() {
            return stuffService.getListByDepartment(userToken, deptId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);
            when(stuffDao.getListByDepartment(deptId))
                    .thenReturn(stuffList);

            TestHelper.listCompareTest(
                    this::execMethod,
                    stuffList
            );
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`departmentId`가 잘못 주어졌을 시]_[InvalidIndexException]")
        public void ERROR_wrongDepartmentId_invalidIndexException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);
            when(stuffDao.getListByDepartment(deptId)).thenThrow(InvalidIndexException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Override
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }

        private List<StuffDto> getStuffListByDept(DepartmentDto dept) {
            return stub.ALL_STUFFS.stream()
                    .filter((stuff) -> dept.matchId(stuff.department()))
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById extends StuffNestedTest {
        private StuffDto stuff;
        private UUID stuffId;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.USER));
            setStuff(randomStuffOnDept(dept));
        }

        private void setStuff(StuffDto stuff) {
            this.stuff = stuff;
            this.stuffId = stuff.id();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.getById(userToken, stuffId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenReturn(stuff);

            TestHelper.objectCompareTest(this::execMethod, stuff);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`에 `stuff`가 존재하지 않을 시]_[NotFoundException")
        public void ERROR_stuffNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
        }

        @Override
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(stuffId)).thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public final class TestCreate extends StuffNestedTest {
        private StuffDto stuff;
        private UUID stuffId;
        private String stuffName;
        private String stuffThumbnail;

        private Integer amount;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
            setStuff(randomStuffOnDept(dept).withItems(new ArrayList<>()));
            amount = 5;
        }

        private void setStuff(StuffDto stuff) {
            this.stuff = stuff;
            this.stuffId = stuff.id();
            this.stuffName = stuff.name();
            this.stuffThumbnail = stuff.thumbnail();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.create(
                    userToken, deptId, stuffName, stuffThumbnail, amount
            );
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.USER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`amount`가 `null`일 시]")
        public void SUCCESS_amountIsZero() {
            setUpDefault();
            amount = null;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);
            when(stuffDao.create(any(), eq(deptId), eq(stuffName), eq(stuffThumbnail))).thenReturn(stuff);

            execMethod();

            verify(stuffDao).create(any(), eq(deptId), eq(stuffName), eq(stuffThumbnail));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`amount`가 0이 아닐 시]")
        public void SUCCESS_amountIsNotZero() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);
            when(stuffDao.create(any(), eq(deptId), eq(stuffName), eq(stuffThumbnail))).thenReturn(stuff);
            for (int i = 0; i < amount; i++) {
                ItemDto newItem = new ItemDto(UUID.randomUUID(), stuff, i + 1, null);
                when(itemDao.create(any(), eq(stuffId), eq(i + 1))).thenReturn(newItem);
            }

            StuffDto expected = stuff;
            for (int i = 0; i < amount; i++) {
                ItemDto newItem = new ItemDto(UUID.randomUUID(), stuff, i + 1, null);
                expected = expected.withItemAdd(newItem);
            }
            execMethod();

            verify(stuffDao).create(any(), eq(deptId), eq(stuffName), eq(stuffThumbnail));
            for (int i = 0; i < amount; i++) {
                verify(itemDao).create(any(), eq(stuffId), eq(i + 1));
            }
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);
            when(stuffDao.create(any(), eq(deptId), eq(stuffName), eq(stuffThumbnail))).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`amount`가 50을 초과할 시]_[ExceedMaxItemNumException]")
        public void ERROR_amountIsUpperThanBound_ExceedMaxItemNumException() {
            setUpDefault();
            amount = 51;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);

            TestHelper.exceptionTest(this::execMethod, ItemAmountLimitExceededException.class);
        }

        @Override
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId)).thenReturn(dept);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public final class TestUpdate extends StuffNestedTest {
        private StuffDto targetStuff;
        private UUID targetStuffId;

        private String newStuffName;
        private String newStuffThumbnail;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
            setStuff(randomStuffOnDept(dept));
            newStuffName = "changed";
            newStuffThumbnail = "𝌡";
        }

        private void setStuff(StuffDto stuff) {
            this.targetStuff = stuff;
            this.targetStuffId = stuff.id();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.update(
                    userToken, targetStuffId, newStuffName, newStuffThumbnail
            );
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.USER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newStuffName`과 `newStuffThumbnail`가 모두 `null`이 아닐 시]")
        public void SUCCESS_allMemberIsNotNull() {
            setUpDefault();
            StuffDto expected = targetStuff
                    .withName(newStuffName)
                    .withThumbnail(newStuffThumbnail);

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(targetStuffId)).thenReturn(targetStuff);
            when(stuffDao.update(targetStuffId, targetStuff.department().id(), newStuffName, newStuffThumbnail))
                    .thenReturn(expected);

            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).update(targetStuffId, targetStuff.department().id(), newStuffName, newStuffThumbnail);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newStuffName`과 `newStuffThumbnail`가 모두 `null`일 시]")
        public void SUCCESS_allMemberIsNull() {
            setUpDefault();
            newStuffName = null;
            newStuffThumbnail = null;
            StuffDto expected = targetStuff;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(targetStuffId)).thenReturn(targetStuff);

            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao, never()).update(any(), any(), any(), any());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newStuffName`만 `null`일 시]")
        public void SUCCESS_someMemberIsNull() {
            setUpDefault();
            newStuffName = null;
            StuffDto expected = targetStuff
                    .withThumbnail(newStuffThumbnail);

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(targetStuffId)).thenReturn(targetStuff);
            when(stuffDao.update(targetStuffId, targetStuff.department().id(), targetStuff.name(), newStuffThumbnail))
                    .thenReturn(expected);

            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).update(targetStuffId, targetStuff.department().id(), targetStuff.name(), newStuffThumbnail);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_stuffNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(targetStuffId)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(targetStuffId)).thenReturn(targetStuff);
            when(stuffDao.update(targetStuffId, targetStuff.department().id(), newStuffName, newStuffThumbnail))
                    .thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @Override
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequesterAccessDenied();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getById(targetStuffId)).thenReturn(targetStuff);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    private abstract class StuffNestedTest extends BaseNestedTestWithDept {
    }
}
