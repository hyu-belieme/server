package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.DepartmentService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest extends BaseServiceTest {
    @InjectMocks
    private DepartmentService departmentService;

    @Nested
    @DisplayName("getAccessibleList()")
    public class TestGetAccessibleList {
        @Test
        @DisplayName("[SUCCESS]_[개발자는 모든 `Department`에 접근 가능하다]_[-]")
        public void SUCCESS_developerGetAccessibleList() {
            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            when(departmentDao.getAllDepartmentsData()).thenReturn(ALL_DEPTS);

            TestHelper.listCompareTest(
                    () -> departmentService.getAccessibleList(userToken),
                    ALL_DEPTS
            );
        }

        @Test
        @DisplayName("[SUCCESS]_[일반 유저는 일부 `Department`에만 접근 가능하다]_[-]")
        public void SUCCESS_userGetAccessibleList() {
            UserDto requester = HYU_CSE_MASTER_USER;
            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getAllDepartmentsData()).thenReturn(ALL_DEPTS);

            List<DepartmentDto> expected = new ArrayList<>();
            for(DepartmentDto department : ALL_DEPTS) {
                if(requester.getMaxPermission(department).hasUserPermission()) {
                    expected.add(department);
                }
            }
            TestHelper.listCompareTest(
                    () -> departmentService.getAccessibleList(userToken),
                    expected
            );
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            assertThrows(UnauthorizedException.class,
                    () -> departmentService.getAccessibleList(userToken));
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex {
        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            String targetUniversityCode = "HYU";
            String targetDepartmentCode = "CSE";

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            when(
                    departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode)
            ).thenReturn(HYU_CSE_DEPT);

            TestHelper.objectCompareTest(
                    () -> departmentService.getByIndex(userToken, targetUniversityCode, targetDepartmentCode),
                    HYU_CSE_DEPT
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`에 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_departmentNotFound_NotFoundException() {
            String targetUniversityCode = "HYU";
            String targetDepartmentCode = "CSE";

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode))
                    .thenThrow(NotFoundException.class);

            assertThrows(NotFoundException.class,
                    () -> departmentService.getByIndex(userToken, targetUniversityCode, targetDepartmentCode)
            );
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            assertThrows(UnauthorizedException.class,
                    () -> departmentService.getByIndex(userToken, "", "")
            );
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            when(userDao.getByToken(userToken)).thenReturn(HYU_CSE_MASTER_USER);

            assertThrows(
                    ForbiddenException.class,
                    () -> departmentService.getByIndex(userToken, "", "")
            );
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private DepartmentDto wannaCreate;
        private String newUniversityCode;
        private String newDepartmentCode;
        private String newName;
        private List<MajorDto> newBaseMajors;
        private List<String> newBaseMajorCodes;

        private void setUp(DepartmentDto wannaCreate) {
            this.wannaCreate = wannaCreate;
            this.newUniversityCode = wannaCreate.university().code();
            this.newDepartmentCode = wannaCreate.code();
            this.newName = wannaCreate.name();
            this.newBaseMajors = wannaCreate.baseMajors();
            this.newBaseMajorCodes = new ArrayList<>();
            this.newBaseMajors.forEach((major)-> newBaseMajorCodes.add(major.code()));
        }

        private void execMethod() {
            departmentService.create(userToken, newUniversityCode, newDepartmentCode, newName, newBaseMajorCodes);
        }

        @Test
        @DisplayName("[SUCCESS]_[`major code`가 모두 기존에 존재할 시]_[-]")
        public void SUCCESS_noNewMajorCode() {
            setUp(HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            // TODO majorDao.getMajorByIndex() 필요함
            for(String majorCode : newBaseMajorCodes) {
//                when(majorDao.getMajorByIndex(newUniversityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }

            execMethod();

            verify(departmentDao).addDepartmentData(wannaCreate);
            verify(majorDao, never()).addMajorData(any());
        }

        @Test
        @DisplayName("[SUCCESS]_[새로운 major_code 등장했을 시]_[-]")
        public void SUCCESS_hasNewMajorCode() {
            setUp(HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            // TODO majorDao.getMajorByIndex() 필요함
            for(int i = 0; i < newBaseMajorCodes.size(); i++) {
                String majorCode = newBaseMajorCodes.get(i);
                if(i == 0) {
//                    when(majorDao.getMajorByIndex(newUniversityCode, majorCode))
//                            .thenThrow(NotFoundException.class);
                    continue;
                }
//                when(majorDao.getMajorByIndex(newUniversityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }

            execMethod();

            verify(departmentDao).addDepartmentData(wannaCreate);
            verify(majorDao, times(1)).addMajorData(newBaseMajors.get(0));
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUp(HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            // TODO InvalidException 필요
//            when(universityDao.getUniversityByCodeData(newUniversityCode))
//                    .thenThrow(InvalidIndexException.class);

            // TODO InvalidException 필요
//            assertThrows(InvalidIndexException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 이미 존재할 시]_[ConflictException]")
        public void departmentConflict_ConflictException() {
            setUp(HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            // TODO majorDao.getMajorByIndex() 필요함
            for(String majorCode : newBaseMajorCodes) {
//                when(majorDao.getMajorByIndex(universityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }
            when(departmentDao.addDepartmentData(wannaCreate)).thenThrow(ConflictException.class);

            assertThrows(ConflictException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUp(HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            assertThrows(UnauthorizedException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUp(HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(HYU_CSE_MASTER_USER);

            assertThrows(ForbiddenException.class, this::execMethod);
        }
    }

    @Nested
    @DisplayName("getUpdate()")
    public class TestUpdate {
        private String targetUniversityCode;
        private String targetDepartmentCode;

        private DepartmentDto newDepartment;
        private String newDepartmentCode;
        private String newName;
        private List<MajorDto> newBaseMajors;
        private List<String> newBaseMajorCodes;

        private void setUp(
                String targetUniversityCode,
                String targetDepartmentCode, DepartmentDto newDepartment
        ) {
            this.targetUniversityCode = targetUniversityCode;
            this.targetDepartmentCode = targetDepartmentCode;
            this.newDepartment = newDepartment;
            this.newDepartmentCode = newDepartment.code();
            this.newName = newDepartment.name();
            this.newBaseMajors = newDepartment.baseMajors();
            this.newBaseMajorCodes = new ArrayList<>();
            this.newBaseMajors.forEach((major)-> newBaseMajorCodes.add(major.code()));
        }

        private void execMethod() {
            departmentService.update(
                    userToken, targetUniversityCode, targetDepartmentCode,
                    newDepartmentCode, newName, newBaseMajorCodes);
        }

        // TODO new Department 세팅에 유의해야 함
        @Test
        @DisplayName("[SUCCESS]_[모든 멤버 변경하고 `major_code`가 모두 기존의 것일 시]_[-]")
        public void SUCCESS_allMemberIsNotNullWithNoMajorAdd() {
            setUp("", "", HYU_CSE_DEPT);

            when(userDao.getByToken(userToken))
                    .thenReturn(DEV_USER);
            when(universityDao.getUniversityByCodeData(targetUniversityCode))
                    .thenReturn(newDepartment.university());
            // TODO majorDao.getMajorByIndex() 필요함
            for(String majorCode : newBaseMajorCodes) {
//                when(majorDao.getMajorByIndex(targetUniversityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }

            execMethod();

            verify(departmentDao).updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment);
            verify(majorDao, never()).addMajorData(any());
        }

        @Test
        @DisplayName("[SUCCESS]_[모든 멤버 변경하고 새로운 `major_code`가 존재할 시]_[-]")
        public void SUCCESS_allMemberIsNotNullWithMajorAdd() {
            setUp("", "", HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            when(universityDao.getUniversityByCodeData(targetUniversityCode))
                    .thenReturn(newDepartment.university());
            // TODO majorDao.getMajorByIndex() 필요함
            for(int i = 0; i < newBaseMajorCodes.size(); i++) {
                String majorCode = newBaseMajorCodes.get(i);
                if(i == 0) {
//                    when(majorDao.getMajorByIndex(targetUniversityCode, majorCode))
//                            .thenThrow(NotFoundException.class);
                    continue;
                }
//                when(majorDao.getMajorByIndex(targetUniversityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }

            execMethod();

            verify(departmentDao).updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment);
            verify(majorDao, times(1)).addMajorData(newBaseMajors.get(0));
        }

        @Test
        @DisplayName("[SUCCESS]_[`newDepartmentCode`, `baseMajor`가 `null`일 시]_[-]")
        public void SUCCESS_someMemberIsNull() {
            setUp("", "", HYU_CSE_DEPT);
            newDepartmentCode = null;
            newBaseMajors = null;
            newBaseMajorCodes = null;

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            when(universityDao.getUniversityByCodeData(targetUniversityCode))
                    .thenReturn(newDepartment.university());
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode))
                    .thenReturn(newDepartment.withName(newName+"AAA"));

            execMethod();

            verify(departmentDao).updateDepartmentData(
                    targetUniversityCode, targetDepartmentCode,
                    newDepartment.withCode(targetDepartmentCode)
            );
            verify(majorDao, never()).addMajorData(any());
        }

        @Test
        @DisplayName("[ERROR]_[변경하고자 하는 `department`를 제외한 새로운 `index`의 `department`가 이미 존재할 시]_[ConflictException]")
        public void departmentConflict_ConflictException() {
            setUp(targetUniversityCode, targetDepartmentCode, HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            // TODO majorDao.getMajorByIndex() 필요함
            for(String majorCode : newBaseMajorCodes) {
//                when(majorDao.getMajorByIndex(universityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }
            when(departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment))
                    .thenThrow(ConflictException.class);

            assertThrows(ConflictException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[변경하고자 하는 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void targetDepartmentNotFound_NotFoundException() {
            setUp(targetUniversityCode, targetDepartmentCode, HYU_CSE_DEPT);

            when(userDao.getByToken(userToken)).thenReturn(DEV_USER);
            // TODO majorDao.getMajorByIndex() 필요함
            for(String majorCode : newBaseMajorCodes) {
//                when(majorDao.getMajorByIndex(universityCode, majorCode))
//                        .thenReturn(baseMajors.get(i));
            }
            when(departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment))
                    .thenThrow(NotFoundException.class);

            assertThrows(NotFoundException.class, this::execMethod);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            assertThrows(UnauthorizedException.class,
                    () -> departmentService.update(userToken, "", "", "", "", new ArrayList<>())
            );
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            when(userDao.getByToken(userToken)).thenReturn(HYU_CSE_MASTER_USER);

            assertThrows(
                    ForbiddenException.class,
                    () -> departmentService.update(userToken, "", "", "", "", new ArrayList<>())
            );
        }
    }
}
