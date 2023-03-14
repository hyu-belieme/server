package com.example.beliemeserver.model;

import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.model.service.StuffService;
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
            return stuffService.getListByDepartment(userToken, univCode, deptCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getListByDepartment(univCode, deptCode))
                    .thenReturn(stuffList);

            TestHelper.listCompareTest(
                    this::execMethod,
                    stuffList
            );
        }

        private List<StuffDto> getStuffListByDept(DepartmentDto dept) {
            return stub.ALL_STUFFS.stream()
                    .filter((stuff) -> dept.matchUniqueKey(stuff.department()))
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex extends StuffNestedTest {
        private StuffDto stuff;
        private String stuffName;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.USER));
            setStuff(randomStuffOnDept(dept));
        }

        private void setStuff(StuffDto stuff) {
            this.stuff = stuff;
            this.stuffName = stuff.name();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.getByIndex(userToken, univCode, deptCode, stuffName);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);

            TestHelper.objectCompareTest(
                    this::execMethod,
                    stuff
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`에 `stuff`가 존재하지 않을 시]_[NotFoundException")
        public void ERROR_stuffNotFound_NotFoundException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
        }
    }

    @Nested
    @DisplayName("create()")
    public final class TestCreate extends StuffNestedTest {
        private StuffDto stuff;
        private String stuffName;
        private String stuffEmoji;

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
            this.stuffName = stuff.name();
            this.stuffEmoji = stuff.emoji();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.create(
                    userToken, univCode, deptCode,
                    stuffName, stuffEmoji, amount
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

            mockDepartmentAndRequester();
            when(stuffDao.create(stuff)).thenReturn(stuff);

            TestHelper.objectCompareTest(this::execMethod, stuff);

            verify(stuffDao).create(stuff);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`amount`가 0이 아닐 시]")
        public void SUCCESS_amountIsNotZero() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.create(stuff)).thenReturn(stuff);
            for (int i = 0; i < amount; i++) {
                ItemDto newItem = ItemDto.init(stuff, i + 1);
                when(itemDao.create(newItem)).thenReturn(newItem);
            }

            StuffDto expected = stuff;
            for (int i = 0; i < amount; i++) {
                ItemDto newItem = ItemDto.init(stuff, i + 1);
                expected = expected.withItemAdd(newItem);
            }
            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).create(stuff);
            for (int i = 0; i < amount; i++) {
                verify(itemDao).create(ItemDto.init(stuff, i + 1));
            }
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.create(stuff)).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`amount`가 50을 초과할 시]_[ExceedMaxItemNumException]")
        public void ERROR_amountIsUpperThanBound_ExceedMaxItemNumException() {
            setUpDefault();
            amount = 51;

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ItemAmountLimitExceededException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public final class TestUpdate extends StuffNestedTest {
        private StuffDto targetStuff;
        private String targetStuffName;

        private String newStuffName;
        private String newStuffEmoji;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserHaveMorePermissionOnDept(dept, Permission.STAFF));
            setStuff(randomStuffOnDept(dept));
            newStuffName = "changed";
            newStuffEmoji = "𝌡";
        }

        private void setStuff(StuffDto stuff) {
            this.targetStuff = stuff;
            this.targetStuffName = stuff.name();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.update(
                    userToken, univCode, deptCode,
                    targetStuffName, newStuffName, newStuffEmoji
            );
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessPermissionOnDept(dept, Permission.USER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newStuffName`과 `newStuffEmoji`가 모두 `null`이 아닐 시]")
        public void SUCCESS_allMemberIsNotNull() {
            setUpDefault();
            StuffDto newStuff = StuffDto.init(dept, newStuffName, newStuffEmoji);
            StuffDto expected = targetStuff
                    .withName(newStuffName)
                    .withEmoji(newStuffEmoji);

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, targetStuffName))
                    .thenReturn(targetStuff);
            when(stuffDao.update(univCode, deptCode, targetStuffName, newStuff))
                    .thenReturn(expected);

            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).update(univCode, deptCode, targetStuffName, newStuff);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newStuffName`과 `newStuffEmoji`가 모두 `null`일 시]")
        public void SUCCESS_allMemberIsNull() {
            setUpDefault();
            newStuffName = null;
            newStuffEmoji = null;
            StuffDto expected = targetStuff;

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, targetStuffName))
                    .thenReturn(targetStuff);

            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao, never()).update(any(), any(), any(), any());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newStuffName`만 `null`일 시]")
        public void SUCCESS_someMemberIsNull() {
            setUpDefault();
            newStuffName = null;
            StuffDto newStuff = StuffDto.init(dept, targetStuffName, newStuffEmoji);
            StuffDto expected = targetStuff
                    .withEmoji(newStuffEmoji);

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, targetStuffName))
                    .thenReturn(targetStuff);
            when(stuffDao.update(univCode, deptCode, targetStuffName, newStuff))
                    .thenReturn(expected);

            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).update(univCode, deptCode, targetStuffName, newStuff);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_stuffNotFound_NotFoundException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, targetStuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();
            StuffDto newStuff = StuffDto.init(dept, newStuffName, newStuffEmoji);

            mockDepartmentAndRequester();
            when(stuffDao.update(univCode, deptCode, targetStuffName, newStuff))
                    .thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    private abstract class StuffNestedTest extends BaseNestedTestWithDept {
    }
}
