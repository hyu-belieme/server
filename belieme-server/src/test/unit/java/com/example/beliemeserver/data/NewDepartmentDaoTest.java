package com.example.beliemeserver.data;

import com.example.beliemeserver.data.daoimpl._new.NewDepartmentDaoImpl;
import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.entity._new.NewMajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity._new.NewMajorEntity;
import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.error.exception.ConflictException;
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
public class NewDepartmentDaoTest  extends BaseDaoTest {
    @InjectMocks
    private NewDepartmentDaoImpl departmentDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<DepartmentDto> execMethod() {
            return departmentDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(deptRepository.findAll()).thenReturn(stub.ALL_DEPTS);
            TestHelper.listCompareTest(this::execMethod, toDepartmentDtoList(stub.ALL_DEPTS));
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

        protected List<DepartmentDto> execMethod() {
            return departmentDao.getListByUniversity(univId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUniv(randomUniv());

            List<NewDepartmentEntity> targets = new ArrayList<>();
            for(NewDepartmentEntity dept : stub.ALL_DEPTS) {
                if(dept.getUniversity().getId().equals(univId)) targets.add(dept);
            }

            when(deptRepository.findByUniversityId(univId)).thenReturn(targets);
            TestHelper.listCompareTest(this::execMethod, toDepartmentDtoList(targets));
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById {
        private NewDepartmentEntity dept;
        private UUID deptId;

        private void setDept(NewDepartmentEntity dept) {
            this.dept = dept;
            this.deptId = dept.getId();
        }

        protected DepartmentDto execMethod() {
            return departmentDao.getById(deptId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setDept(randomDept());

            when(deptRepository.findById(deptId)).thenReturn(Optional.of(dept));
            TestHelper.objectCompareTest(this::execMethod, dept.toDepartmentDto());
        }

        @Test
        @DisplayName("[ERROR]_[`deptId`에 해당하는 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setDept(randomDept());

            when(deptRepository.findById(deptId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("checkExistByIndex()")
    public class TestCheckByIndex {
        private NewDepartmentEntity dept;
        private UUID deptId;

        private void setDept(NewDepartmentEntity dept) {
            this.dept = dept;
            this.deptId = dept.getId();
        }

        protected boolean execMethod() {
            return departmentDao.checkExistById(deptId);
        }

        @Test()
        @DisplayName("[SUCCESS]_[`id`에 대응하는 `department`가 존재할 시]_[-]")
        public void SUCCESS_exist() {
            setDept(randomDept());

            when(deptRepository.existsById(deptId)).thenReturn(true);

            TestHelper.objectCompareTest(this::execMethod, true);
        }

        @Test()
        @DisplayName("[SUCCESS]_[`index`에 대응하는 `department`가 존재하지 않을 시]_[-]")
        public void SUCCESS_notExist() {
            setDept(randomDept());

            when(deptRepository.existsById(deptId)).thenReturn(false);

            TestHelper.objectCompareTest(this::execMethod, false);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private NewDepartmentEntity dept;

        private void setDept(NewDepartmentEntity department) {
            this.dept = department;
        }

        protected DepartmentDto execMethod() {
            return departmentDao.create(dept.toDepartmentDto());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setDept(randomDept());

            when(univRepository.findById(dept.getUniversityId())).thenReturn(Optional.of(dept.getUniversity()));
            when(deptRepository.existsByUniversityIdAndName(dept.getUniversityId(), dept.getName())).thenReturn(false);
            for(NewMajorDepartmentJoinEntity majorJoin : dept.getBaseMajorJoin()) {
                when(majorDeptJoinRepository.save(mockArg(majorJoin))).thenReturn(majorJoin);
            }
            when(deptRepository.save(mockArg(dept))).thenReturn(dept);

            TestHelper.objectCompareTest(this::execMethod, dept.toDepartmentDto());

            for(NewMajorDepartmentJoinEntity majorJoin : dept.getBaseMajorJoin()) {
                verify(majorDeptJoinRepository).save(majorJoin);
            }
            verify(deptRepository).save(dept);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `department`가 존재할 시]_[ConflictException]")
        public void ERROR_indexConflict_ConflictException() {
            setDept(randomDept());

            when(univRepository.findById(dept.getUniversityId())).thenReturn(Optional.of(dept.getUniversity()));
            when(deptRepository.existsByUniversityIdAndName(dept.getUniversityId(), dept.getName())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`index`에 해당하는 `university`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_universityNotExist_NotFoundException() {
            setDept(randomDept());

            when(univRepository.findById(dept.getUniversityId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        private NewDepartmentEntity target;
        private UUID targetId;
        private NewDepartmentEntity newDept;

        private void setTarget(NewDepartmentEntity dept) {
            this.target = dept;
            this.targetId = dept.getId();
        }

        private NewDepartmentEntity savedDept() {
            return target
                    .withUniversity(newDept.getUniversity())
                    .withName(newDept.getName())
                    .withBaseMajor(newDept.getBaseMajorJoin());
        }

        protected DepartmentDto execMethod() {
            return departmentDao.update(targetId, newDept.toDepartmentDto());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[index(name)을 변경하지 않을 시]_[-]")
        public void SUCCESS_notChangeIndex() {
            setTarget(randomDept());

            newDept = target;
            newDept = addNewMajor(newDept);
            newDept = addNewMajor(newDept);

            when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newDept.getUniversityId())).thenReturn(Optional.of(newDept.getUniversity()));

            for(NewMajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
                when(majorRepository.findById(majorJoin.getMajorId())).thenReturn(Optional.of(majorJoin.getMajor()));
                when(majorDeptJoinRepository.save(mockArg(majorJoin))).thenReturn(majorJoin);
            }
            when(deptRepository.save(mockArg(savedDept()))).thenReturn(savedDept());

            TestHelper.objectCompareTest(this::execMethod, savedDept().toDepartmentDto());

            for(NewMajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
                verify(majorDeptJoinRepository).save(mockArg(majorJoin));
            }
            verify(deptRepository).save(mockArg(savedDept()));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[index(name)를 변경할 시]_[-]")
        public void SUCCESS_changeIndex() {
            setTarget(randomDept());

            newDept = target.withName("__NEW_NAME");
            newDept = addNewMajor(newDept);
            newDept = addNewMajor(newDept);

            when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newDept.getUniversityId())).thenReturn(Optional.of(newDept.getUniversity()));
            when(deptRepository.existsByUniversityIdAndName(newDept.getUniversityId(), newDept.getName())).thenReturn(false);

            for(NewMajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
                when(majorRepository.findById(majorJoin.getMajorId())).thenReturn(Optional.of(majorJoin.getMajor()));
                when(majorDeptJoinRepository.save(mockArg(majorJoin))).thenReturn(majorJoin);
            }
            when(deptRepository.save(mockArg(savedDept()))).thenReturn(savedDept());

            TestHelper.objectCompareTest(this::execMethod, savedDept().toDepartmentDto());

            for(NewMajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
                verify(majorDeptJoinRepository).save(mockArg(majorJoin));
            }
            verify(deptRepository).save(mockArg(savedDept()));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`id`에 해당하는 `department`가 없을 시]_[NotFoundException]")
        public void ERROR_targetNotFound_NotFoundException() {
            setTarget(randomDept());

            newDept = target.withName("__NEW_NAME");
            newDept = addNewMajor(newDept);
            newDept = addNewMajor(newDept);

            when(deptRepository.findById(targetId)).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`NewDept`의 `index`에 해당하는 `university`가 없을 시]_[NotFoundException]")
        public void ERROR_univOfNewDeptNotFound_NotFoundException() {
            setTarget(randomDept());

            newDept = target.withName("__NEW_NAME");
            newDept = addNewMajor(newDept);
            newDept = addNewMajor(newDept);

            when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newDept.getUniversityId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`NewDept`의 `index`에 해당하는 `department`가 기존에 있을 시]_[ConflictException]")
        public void ERROR_newDeptConflict_ConflictException() {
            setTarget(randomDept());

            newDept = target.withName("__NEW_NAME");
            newDept = addNewMajor(newDept);
            newDept = addNewMajor(newDept);

            when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newDept.getUniversityId())).thenReturn(Optional.of(newDept.getUniversity()));
            when(deptRepository.existsByUniversityIdAndName(newDept.getUniversityId(), newDept.getName())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        private NewDepartmentEntity addNewMajor(NewDepartmentEntity dept) {
            NewMajorEntity newMajor = randomMajorNotInDept(dept);
            if(newMajor == null) return dept;
            return dept.withBaseMajorAdd(new NewMajorDepartmentJoinEntity(newMajor, dept));
        }
    }

    private NewMajorEntity randomMajorNotInDept(NewDepartmentEntity dept) {
        return randomSelectAndLog(majorsNotInDept(allMajors(), dept));
    }

    private RandomGetter<NewMajorEntity> majorsNotInDept(RandomGetter<NewMajorEntity> majors, NewDepartmentEntity dept) {
        return majors.filter((major) ->
                major.getUniversityId().equals(dept.getUniversityId()) && !doesDeptContainMajorJoin(dept, major));
    }

    private boolean doesDeptContainMajorJoin(NewDepartmentEntity dept, NewMajorEntity major) {
        for(NewMajorDepartmentJoinEntity majorJoin : dept.getBaseMajorJoin()) {
            if(majorJoin.getMajor().equals(major)) return true;
        }
        return false;
    }
}
