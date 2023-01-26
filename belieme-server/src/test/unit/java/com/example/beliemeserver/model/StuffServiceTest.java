package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.StuffService;
import com.example.beliemeserver.util.StubHelper;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StuffServiceTest extends BaseServiceTest {
    @InjectMocks
    private StuffService stuffService;

    @Nested
    @DisplayName("getListByDepartment()")
    public class TestGetListByDepartment {
        private DepartmentDto department;
        private String universityCode;
        private String departmentCode;

        private UserDto requester;
        private List<StuffDto> expected;

        private void setUpDefault() {
            department = HYU_CSE_DEPT;
            universityCode = department.university().code();
            departmentCode = department.code();

            requester = HYU_CSE_NORMAL_2_USER;

            expected = getStuffListByDepartmentFromStub(department);
        }

        private void setUp(DepartmentDto department, UserDto requester) {
            this.department = department;
            this.universityCode = department.university().code();
            this.departmentCode = department.code();

            this.requester = requester;

            expected = getStuffListByDepartmentFromStub(department);
        }

        private List<StuffDto> execMethod() {
            return stuffService.getListByDepartment(userToken, universityCode, departmentCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(stuffDao.getListByDepartment(universityCode, departmentCode))
                    .thenReturn(expected);

            TestHelper.listCompareTest(
                    this::execMethod,
                    expected
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenThrow(NotFoundException.class);

            assertThrows(InvalidIndexException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            assertThrows(UnauthorizedException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            requester = HYU_CSE_BANNED_USER;

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);

            assertThrows(ForbiddenException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[`requester`가 다른 학과의 권한만 갖고 있을 시]_[ForbiddenException]")
        public void ERROR_accessDenied2_ForbiddenException() {
            setUpDefault();
            requester = CKU_DUMMY_USER_2;

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);

            assertThrows(ForbiddenException.class, this::execMethod);
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
}
