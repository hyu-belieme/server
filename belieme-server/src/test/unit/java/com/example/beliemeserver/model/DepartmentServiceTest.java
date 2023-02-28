package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.service.DepartmentService;
import com.example.beliemeserver.util.RandomGetter;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest extends BaseServiceTest {
    @InjectMocks
    private DepartmentService departmentService;

    @Nested
    @DisplayName("getAccessibleList()")
    public class TestGetAccessibleList extends DeptNestedTest {
        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
        }

        @Override
        protected List<DepartmentDto> execMethod() {
            return departmentService.getAccessibleList(userToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[개발자는 모든 `Department`에 접근 가능하다]_[-]")
        public void SUCCESS_developerGetAccessibleList() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getAllList()).thenReturn(stub.ALL_DEPTS);

            TestHelper.listCompareTest(this::execMethod, stub.ALL_DEPTS);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[일반 유저는 일부 `Department`에만 접근 가능하다]_[-]")
        public void SUCCESS_userGetAccessibleList() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getAllList()).thenReturn(stub.ALL_DEPTS);

            List<DepartmentDto> expected = new ArrayList<>();
            for (DepartmentDto department : stub.ALL_DEPTS) {
                if (requester.getMaxPermission(department).hasUserPermission()) {
                    expected.add(department);
                }
            }
            TestHelper.listCompareTest(this::execMethod, expected);
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex extends DeptNestedTest {
        private DepartmentDto dept;
        private String univCode;
        private String deptCode;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setDept(randomDept());
        }

        private void setDept(DepartmentDto dept) {
            this.dept = dept;
            this.univCode = dept.university().code();
            this.deptCode = dept.code();
        }

        @Override
        protected DepartmentDto execMethod() {
            return departmentService.getByIndex(userToken, univCode, deptCode);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getByIndex(univCode, deptCode))
                    .thenReturn(dept);

            TestHelper.objectCompareTest(this::execMethod, dept);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`에 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_departmentNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getByIndex(univCode, deptCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate extends DeptNestedTest {
        private DepartmentDto dept;
        private UniversityDto univ;
        private String univCode;
        private String deptCode;
        private String name;
        private List<MajorDto> baseMajors;
        private List<String> baseMajorCodes;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setDept(randomDeptHaveMajors());
        }

        private void setDept(DepartmentDto dept) {
            this.dept = dept;
            this.univ = dept.university();
            this.univCode = dept.university().code();
            this.deptCode = dept.code();
            this.name = dept.name();
            this.baseMajors = dept.baseMajors();
            this.baseMajorCodes = new ArrayList<>();
            dept.baseMajors().forEach((major) -> baseMajorCodes.add(major.code()));
        }

        protected DepartmentDto execMethod() {
            return departmentService.create(
                    userToken, univCode, deptCode, name, baseMajorCodes);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`major code`가 모두 기존에 존재할 시]_[-]")
        public void SUCCESS_noNewMajorCode() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(univCode))
                    .thenReturn(univ);
            when(departmentDao.create(dept)).thenReturn(dept);
            mockGetMajors(baseMajors, univ, baseMajorCodes);

            execMethod();

            verify(departmentDao).create(dept);
            verify(majorDao, never()).create(any());
            for (AuthorityDto.Permission permission : AuthorityDto.Permission.values()) {
                verify(authorityDao).create(new AuthorityDto(dept, permission));
            }
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[새로운 major_code 등장했을 시]_[-]")
        public void SUCCESS_hasNewMajorCode() {
            setUpDefault();
            setDept(randomDeptHaveMajors());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(univCode))
                    .thenReturn(univ);
            when(departmentDao.create(dept)).thenReturn(dept);
            mockGetOrCreateMajors(baseMajors, univ, baseMajorCodes);

            execMethod();

            verify(departmentDao).create(dept);
            verify(majorDao, times(1)).create(baseMajors.get(0));
            for (AuthorityDto.Permission permission : AuthorityDto.Permission.values()) {
                verify(authorityDao).create(new AuthorityDto(dept, permission));
            }
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(univCode))
                    .thenThrow(NotFoundException.class);

            assertThrows(InvalidIndexException.class, this::execMethod);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 이미 존재할 시]_[ConflictException]")
        public void departmentConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(univCode))
                    .thenReturn(univ);
            when(departmentDao.create(dept)).thenThrow(ConflictException.class);
            mockGetMajors(baseMajors, univ, baseMajorCodes);

            assertThrows(ConflictException.class, this::execMethod);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            assertThrows(ForbiddenException.class, this::execMethod);
        }
    }

    @Nested
    @DisplayName("getUpdate()")
    public class TestUpdate extends DeptNestedTest {
        private DepartmentDto targetDept;
        private UniversityDto targetUniv;
        private String targetUnivCode;
        private String targetDeptCode;

        private String newDeptCode;
        private String newName;
        private List<MajorDto> newBaseMajors;
        private List<String> newBaseMajorCodes;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setTargetDept(randomDept());
        }

        private void setTargetDept(DepartmentDto dept) {
            this.targetDept = dept;
            this.targetUniv = dept.university();
            this.targetUnivCode = dept.university().code();
            this.targetDeptCode = dept.code();

            this.newDeptCode = dept.code() + "AAA";
            this.newName = dept.name() + "BBB";
            this.newBaseMajors = dept.baseMajors();
            this.newBaseMajorCodes = new ArrayList<>();
            newBaseMajors.forEach((major) -> newBaseMajorCodes.add(major.code()));
        }

        @Override
        protected DepartmentDto execMethod() {
            return departmentService.update(
                    userToken, targetUnivCode, targetDeptCode,
                    newDeptCode, newName, newBaseMajorCodes);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[모든 멤버 변경하고 `major_code`가 모두 기존의 것일 시]_[-]")
        public void SUCCESS_allMemberIsNotNullWithNoMajorAdd() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetUnivCode))
                    .thenReturn(targetUniv);
            when(departmentDao.getByIndex(targetUnivCode, targetDeptCode))
                    .thenReturn(targetDept);
            mockGetMajors(newBaseMajors, targetUniv, newBaseMajorCodes);

            execMethod();

            DepartmentDto newDept = targetDept
                    .withCode(newDeptCode)
                    .withName(newName)
                    .withBaseMajors(newBaseMajors);
            verify(departmentDao).update(targetUnivCode, targetDeptCode, newDept);
            verify(majorDao, never()).create(any());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[모든 멤버 변경하고 새로운 `major_code`가 존재할 시]_[-]")
        public void SUCCESS_allMemberIsNotNullWithMajorAdd() {
            setUpDefault();
            setTargetDept(randomDeptHaveMajors());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetUnivCode))
                    .thenReturn(targetUniv);
            when(departmentDao.getByIndex(targetUnivCode, targetDeptCode))
                    .thenReturn(targetDept);
            mockGetOrCreateMajors(newBaseMajors, targetUniv, newBaseMajorCodes);

            execMethod();

            DepartmentDto newDept = targetDept
                    .withCode(newDeptCode)
                    .withName(newName)
                    .withBaseMajors(newBaseMajors);
            verify(departmentDao).update(targetUnivCode, targetDeptCode, newDept);
            verify(majorDao, times(1)).create(newBaseMajors.get(0));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`newDepartmentCode`, `baseMajor`가 `null`일 시]_[-]")
        public void SUCCESS_someMemberIsNull() {
            setUpDefault();
            newDeptCode = null;
            newBaseMajors = null;
            newBaseMajorCodes = null;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetUnivCode))
                    .thenReturn(targetUniv);
            when(departmentDao.getByIndex(targetUnivCode, targetDeptCode))
                    .thenReturn(targetDept);

            execMethod();

            DepartmentDto newDept = targetDept.withName(newName);
            verify(departmentDao).update(targetUnivCode, targetDeptCode, newDept);
            verify(majorDao, never()).create(any());
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[변경하고자 하는 `department`를 제외한 새로운 `index`의 `department`가 이미 존재할 시]_[ConflictException]")
        public void departmentConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetUnivCode))
                    .thenReturn(targetUniv);
            when(departmentDao.getByIndex(targetUnivCode, targetDeptCode))
                    .thenReturn(targetDept);
            mockGetMajors(newBaseMajors, targetUniv, newBaseMajorCodes);

            when(departmentDao.update(eq(targetUnivCode), eq(targetDeptCode), any()))
                    .thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void targetUniversityNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetUnivCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[변경하고자 하는 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void targetDepartmentNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetUnivCode))
                    .thenReturn(targetUniv);
            when(departmentDao.getByIndex(targetUnivCode, targetDeptCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }

    private RandomGetter<DepartmentDto> deptsHaveMajors(RandomGetter<DepartmentDto> depts) {
        return depts.filter((dept) -> dept.baseMajors().size() > 0);
    }

    private DepartmentDto randomDeptHaveMajors() {
        return randomSelectAndLog(deptsHaveMajors(allDepts()));
    }

    private abstract class DeptNestedTest extends BaseNestedTest {
        protected void mockGetMajors(List<MajorDto> majors, UniversityDto univ, List<String> majorCodes) {
            for (int i = 0; i < majorCodes.size(); i++) {
                String majorCode = majorCodes.get(i);
                when(majorDao.getByIndex(univ.code(), majorCode))
                        .thenReturn(majors.get(i));
            }
        }

        protected void mockGetOrCreateMajors(List<MajorDto> majors, UniversityDto univ, List<String> majorCodes) {
            for (int i = 0; i < majorCodes.size(); i++) {
                String majorCode = majorCodes.get(i);
                if (i == 0) {
                    when(majorDao.getByIndex(univ.code(), majorCode))
                            .thenThrow(NotFoundException.class);
                    when(majorDao.create(new MajorDto(univ, majorCode)))
                            .thenReturn(majors.get(i));
                    continue;
                }
                when(majorDao.getByIndex(univ.code(), majorCode))
                        .thenReturn(majors.get(i));
            }
        }
    }
}
