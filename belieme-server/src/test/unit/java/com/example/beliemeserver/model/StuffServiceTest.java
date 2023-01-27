package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.StuffService;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.beliemeserver.util.StubHelper.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StuffServiceTest extends BaseServiceTest {
    @InjectMocks
    private StuffService stuffService;

    @Nested
    @DisplayName("getListByDepartment()")
    public final class TestGetListByDepartment extends BaseNestedTestClass {
        private List<StuffDto> expected;

        @Override
        protected void setUpDefault() {
            super.setUp(HYU_CSE_DEPT, HYU_CSE_NORMAL_2_USER);

            expected = getStuffListByDepartmentFromStub(department);
        }

        @Override
        protected List<StuffDto> execMethod() {
            return stuffService.getListByDepartment(userToken, universityCode, departmentCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getListByDepartment(universityCode, departmentCode))
                    .thenReturn(expected);

            TestHelper.listCompareTest(
                    this::execMethod,
                    expected
            );
        }

        private List<StuffDto> getStuffListByDepartmentFromStub(DepartmentDto department) {
            List<StuffDto> output = new ArrayList<>();
            for(StuffDto stuff : ALL_STUFFS) {
                if(stuff.department().matchUniqueKey(department)) {
                    output.add(stuff);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex extends BaseNestedTestClass {
        private StuffDto stuff;
        private String stuffName;

        @Override
        protected void setUpDefault() {
            super.setUp(HYU_CSE_DEPT, HYU_CSE_NORMAL_1_USER);

            stuff = ALL_STUFFS.get(0);
            stuffName = stuff.name();
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.getByIndex(userToken, universityCode, departmentCode, stuffName);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
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
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(
                    this::execMethod,
                    NotFoundException.class
            );
        }
    }

    @Nested
    @DisplayName("create()")
    public final class TestCreate extends BaseNestedTestClass {
        private StuffDto newStuff;
        private String newStuffName;
        private String newStuffEmoji;

        private Integer newStuffAmount;

        @Override
        protected void setUpDefault() {
            newStuff = ALL_STUFFS.get(0).withItems(new ArrayList<>());
            newStuffName = newStuff.name();
            newStuffEmoji = newStuff.emoji();
            newStuffAmount = 5;

            super.setUp(newStuff.department(), HYU_CSE_STAFF_USER);
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.create(
                    userToken, universityCode, departmentCode,
                    newStuffName, newStuffEmoji, newStuffAmount
            );
        }

        @Override
        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_NORMAL_2_USER;
        }

        @Test
        @DisplayName("[SUCCESS]_[`amount`가 `null`일 시]")
        public void SUCCESS_amountIsZero() {
            setUpDefault();
            newStuffAmount = null;

            mockDepartmentAndRequester();
            when(stuffDao.create(newStuff)).thenReturn(newStuff);

            StuffDto expected = newStuff;
            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).create(newStuff);
        }

        @Test
        @DisplayName("[SUCCESS]_[`amount`가 0이 아닐 시]")
        public void SUCCESS_amountIsNotZero() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.create(newStuff)).thenReturn(newStuff);
            for(int i = 0; i < newStuffAmount; i++) {
                ItemDto newItem = ItemDto.init(newStuff, i+1);
                when(itemDao.create(newItem)).thenReturn(newItem);
            }

            StuffDto expected = newStuff;
            for(int i = 0; i < newStuffAmount; i++) {
                ItemDto newItem = ItemDto.init(newStuff, i+1);
                expected = expected.withItemAdd(newItem);
            }
            TestHelper.objectCompareTest(this::execMethod, expected);

            verify(stuffDao).create(newStuff);
            for(int i = 0; i < newStuffAmount; i++) {
                verify(itemDao).create(ItemDto.init(newStuff, i+1));
            }
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.create(newStuff)).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`amount`가 음수일 시]_[MethodNotAllowedException]")
        public void ERROR_amountIsNegative_() {
            setUpDefault();
            newStuffAmount = -1;

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`amount`가 50을 초과할 시]_[MethodNotAllowedException]")
        public void ERROR_amountIsUpperThanBound_() {
            setUpDefault();
            newStuffAmount = 51;

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public final class TestUpdate extends BaseNestedTestClass {
        private StuffDto targetStuff;
        private String targetStuffName;

        private String newStuffName;
        private String newStuffEmoji;

        @Override
        protected void setUpDefault() {
            targetStuff = ALL_STUFFS.get(0).withItems(new ArrayList<>());
            targetStuffName = targetStuff.name();
            newStuffName = "changed";
            newStuffEmoji = "𝌡";

            super.setUp(targetStuff.department(), HYU_CSE_STAFF_USER);
        }

        @Override
        protected StuffDto execMethod() {
            return stuffService.update(
                    userToken, universityCode, departmentCode,
                    targetStuffName, newStuffName, newStuffEmoji
            );
        }

        @Override
        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_NORMAL_2_USER;
        }

        @Test
        @DisplayName("[SUCCESS]_[`newStuffName`과 `newStuffEmoji`가 모두 `null`이 아닐 시]")
        public void SUCCESS_allMemberIsNotNull() {
            setUpDefault();
            StuffDto newStuff = targetStuff
                    .withName(newStuffName)
                    .withEmoji(newStuffEmoji);

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, targetStuffName))
                    .thenReturn(targetStuff);
            when(stuffDao.update(universityCode, departmentCode, targetStuffName, newStuff))
                    .thenReturn(newStuff);

            TestHelper.objectCompareTest(this::execMethod, newStuff);

            verify(stuffDao).update(universityCode, departmentCode, targetStuffName, newStuff);
        }

        @Test
        @DisplayName("[SUCCESS]_[`newStuffName`과 `newStuffEmoji`가 모두 `null`일 시]")
        public void SUCCESS_allMemberIsNull() {
            setUpDefault();
            newStuffName = null;
            newStuffEmoji = null;
            StuffDto newStuff = targetStuff;

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, targetStuffName))
                    .thenReturn(targetStuff);

            TestHelper.objectCompareTest(this::execMethod, newStuff);

            verify(stuffDao, never()).update(any(), any(), any(), any());
        }

        @Test
        @DisplayName("[SUCCESS]_[`newStuffName`만 `null`일 시]")
        public void SUCCESS_someMemberIsNull() {
            setUpDefault();
            newStuffName = null;
            StuffDto newStuff = targetStuff.withEmoji(newStuffEmoji);

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, targetStuffName))
                    .thenReturn(targetStuff);
            when(stuffDao.update(universityCode, departmentCode, targetStuffName, newStuff))
                    .thenReturn(newStuff);

            TestHelper.objectCompareTest(this::execMethod, newStuff);

            verify(stuffDao).update(universityCode, departmentCode, targetStuffName, newStuff);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_stuffNotFound_NotFoundException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, targetStuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_stuffConflict_ConflictException() {
            setUpDefault();
            StuffDto newStuff = targetStuff
                    .withName(newStuffName)
                    .withEmoji(newStuffEmoji);

            mockDepartmentAndRequester();
            when(stuffDao.update(universityCode, departmentCode, targetStuffName, newStuff))
                    .thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    private abstract class BaseNestedTestClass {
        protected DepartmentDto department;
        protected String universityCode;
        protected String departmentCode;

        protected UserDto requester;

        protected abstract void setUpDefault();
        protected abstract Object execMethod();

        protected void setUp(DepartmentDto department, UserDto requester) {
            this.department = department;
            this.universityCode = department.university().code();
            this.departmentCode = department.code();

            this.requester = requester;
        }

        protected void setRequesterAccessDenied() {
            requester = HYU_CSE_BANNED_USER;
        }

        protected void mockDepartmentAndRequester() {
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
