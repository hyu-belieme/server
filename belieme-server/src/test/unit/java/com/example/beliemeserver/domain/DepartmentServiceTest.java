package com.example.beliemeserver.domain;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.MajorDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.service.DepartmentService;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
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
import java.util.UUID;

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
    @DisplayName("getById()")
    public class TestGetById extends DeptNestedTest {
        private DepartmentDto dept;
        private UUID deptId;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setDept(randomDept());
        }

        private void setDept(DepartmentDto dept) {
            this.dept = dept;
            this.deptId = dept.id();
        }

        @Override
        protected DepartmentDto execMethod() {
            return departmentService.getById(userToken, deptId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId))
                    .thenReturn(dept);

            TestHelper.objectCompareTest(this::execMethod, dept);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`에 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_departmentNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(deptId))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate extends DeptNestedTest {
        private DepartmentDto dept;
        private UUID deptId;
        private UUID univId;

        private String deptName;
        private List<MajorDto> baseMajors;
        private List<UUID> baseMajorIds;
        private List<String> baseMajorCodes;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setDept(randomDeptHaveMajors());
        }

        private void setDept(DepartmentDto dept) {
            this.dept = dept;
            this.deptId = dept.id();
            this.univId = dept.university().id();
            this.deptName = dept.name();
            this.baseMajors = dept.baseMajors();
            this.baseMajorIds = toMajorIds(baseMajors);
            this.baseMajorCodes = toMajorCodes(baseMajors);
        }

        protected DepartmentDto execMethod() {
            return departmentService.create(userToken, univId, deptName, baseMajorCodes);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`major code`가 모두 기존에 존재할 시]_[-]")
        public void SUCCESS_noNewMajorCode() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.create(any(), eq(univId), eq(deptName), eq(baseMajorIds))).thenReturn(dept);
            mockGetMajors(univId, baseMajorCodes, baseMajors);

            execMethod();

            verify(departmentDao).create(any(), eq(univId), eq(deptName), eq(baseMajorIds));
            verify(majorDao, never()).create(any(), any(), any());
            for (Permission permission : Permission.values()) {
                verify(authorityDao).create(deptId, permission);
            }
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[새로운 major_code 등장했을 시]_[-]")
        public void SUCCESS_hasNewMajorCode() {
            setUpDefault();
            setDept(randomDeptHaveMajors());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.create(any(), eq(univId), eq(deptName), eq(baseMajorIds))).thenReturn(dept);
            mockGetOrCreateMajors(univId, baseMajorCodes, baseMajors);

            execMethod();

            verify(departmentDao).create(any(), eq(univId), eq(deptName), eq(baseMajorIds));
            verify(majorDao, times(1)).create(any(), eq(univId), eq(baseMajorCodes.get(0)));
            for (Permission permission : Permission.values()) {
                verify(authorityDao).create(dept.id(), permission);
            }
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.create(any(), eq(univId), eq(deptName), eq(baseMajorIds))).thenThrow(InvalidIndexException.class);
            mockGetOrCreateMajors(univId, baseMajorCodes, baseMajors);

            assertThrows(InvalidIndexException.class, this::execMethod);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 이미 존재할 시]_[ConflictException]")
        public void departmentConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.create(any(), eq(univId), eq(deptName), eq(baseMajorIds)))
                    .thenThrow(ConflictException.class);
            mockGetMajors(univId, baseMajorCodes, baseMajors);

            assertThrows(ConflictException.class, this::execMethod);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            assertThrows(PermissionDeniedException.class, this::execMethod);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate extends DeptNestedTest {
        private DepartmentDto targetDept;
        private UUID targetDeptId;
        private UUID targetDeptUnivId;

        private String newName;
        private List<MajorDto> newBaseMajors;
        private List<UUID> newBaseMajorIds;
        private List<String> newBaseMajorCodes;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setTargetDept(randomDept());
        }

        private void setTargetDept(DepartmentDto dept) {
            this.targetDept = dept;
            this.targetDeptId = dept.id();
            this.targetDeptUnivId = dept.university().id();

            this.newName = dept.name() + "BBB";
            this.newBaseMajors = dept.baseMajors();
            this.newBaseMajorIds = toMajorIds(newBaseMajors);
            this.newBaseMajorCodes = toMajorCodes(newBaseMajors);
        }

        @Override
        protected DepartmentDto execMethod() {
            return departmentService.update(
                    userToken, targetDeptId, newName, newBaseMajorCodes
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[모든 멤버 변경하고 `major_code`가 모두 기존의 것일 시]_[-]")
        public void SUCCESS_allMemberIsNotNullWithNoMajorAdd() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(targetDeptId)).thenReturn(targetDept);
            mockGetMajors(targetDeptUnivId, newBaseMajorCodes, newBaseMajors);

            execMethod();

            verify(departmentDao).update(targetDeptId, targetDeptUnivId, newName, newBaseMajorIds);
            verify(majorDao, never()).create(any(), any(), any());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[모든 멤버 변경하고 새로운 `major_code`가 존재할 시]_[-]")
        public void SUCCESS_allMemberIsNotNullWithMajorAdd() {
            setUpDefault();
            setTargetDept(randomDeptHaveMajors());

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(targetDeptId)).thenReturn(targetDept);
            mockGetOrCreateMajors(targetDeptUnivId, newBaseMajorCodes, newBaseMajors);

            execMethod();

            verify(departmentDao).update(targetDeptId, targetDeptUnivId, newName, newBaseMajorIds);
            verify(majorDao, times(1)).create(any(), eq(targetDeptUnivId), eq(newBaseMajorCodes.get(0)));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`baseMajor`가 `null`일 시]_[-]")
        public void SUCCESS_someMemberIsNull() {
            setUpDefault();
            newBaseMajorCodes = null;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(targetDeptId)).thenReturn(targetDept);

            execMethod();

            verify(departmentDao).update(targetDeptId, targetDeptUnivId, newName, toMajorIds(targetDept.baseMajors()));
            verify(majorDao, never()).create(any(), any(), any());
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[변경하고자 하는 `department`를 제외한 새로운 `index`의 `department`가 이미 존재할 시]_[ConflictException]")
        public void departmentConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(targetDeptId)).thenReturn(targetDept);
            mockGetMajors(targetDeptUnivId, newBaseMajorCodes, newBaseMajors);

            when(departmentDao.update(targetDeptId, targetDeptUnivId, newName, newBaseMajorIds))
                    .thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void targetUniversityNotFound_InvalidIndexException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(targetDeptId)).thenReturn(targetDept);
            mockGetMajors(targetDeptUnivId, newBaseMajorCodes, newBaseMajors);

            when(departmentDao.update(targetDeptId, targetDeptUnivId, newName, newBaseMajorIds))
                    .thenThrow(InvalidIndexException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[변경하고자 하는 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void targetDepartmentNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(departmentDao.getById(targetDeptId)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[PermissionDeniedException]")
        public void ERROR_accessDenied_PermissionDeniedException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, PermissionDeniedException.class);
        }
    }

    private RandomGetter<DepartmentDto> deptsHaveMajors(RandomGetter<DepartmentDto> depts) {
        return depts.filter((dept) -> dept.baseMajors().size() > 0);
    }

    private DepartmentDto randomDeptHaveMajors() {
        return randomSelectAndLog(deptsHaveMajors(allDepts()));
    }

    private List<UUID> toMajorIds(List<MajorDto> majors) {
        return majors.stream().map(MajorDto::id).toList();
    }

    private List<String> toMajorCodes(List<MajorDto> majors) {
        return majors.stream().map(MajorDto::code).toList();
    }

    private abstract class DeptNestedTest extends BaseNestedTest {
        protected void mockGetMajors(UUID univId, List<String> majorCodes, List<MajorDto> majors) {
            for (int i = 0; i < majorCodes.size(); i++) {
                String majorCode = majorCodes.get(i);
                when(majorDao.getByIndex(univId, majorCode))
                        .thenReturn(majors.get(i));
            }
        }

        protected void mockGetOrCreateMajors(UUID univId, List<String> majorCodes, List<MajorDto> majors) {
            for (int i = 0; i < majorCodes.size(); i++) {
                String majorCode = majorCodes.get(i);
                if (i == 0) {
                    when(majorDao.getByIndex(univId, majorCode))
                            .thenThrow(NotFoundException.class);
                    when(majorDao.create(any(), eq(univId), eq(majorCode)))
                            .thenReturn(majors.get(i));
                    continue;
                }
                when(majorDao.getByIndex(univId, majorCode))
                        .thenReturn(majors.get(i));
            }
        }
    }
}
