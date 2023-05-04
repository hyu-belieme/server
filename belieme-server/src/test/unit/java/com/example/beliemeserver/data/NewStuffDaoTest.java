package com.example.beliemeserver.data;

import com.example.beliemeserver.data.daoimpl._new.NewStuffDaoImpl;
import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
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
public class NewStuffDaoTest extends BaseDaoTest {
    @InjectMocks
    private NewStuffDaoImpl stuffDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<StuffDto> execMethod() {
            return stuffDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(stuffRepository.findAll()).thenReturn(stub.ALL_STUFFS);
            TestHelper.listCompareTest(this::execMethod, toStuffDtoList(stub.ALL_STUFFS));
        }
    }

    @Nested
    @DisplayName("getListByDepartment()")
    public class TestGetListByDepartment {
        private NewDepartmentEntity dept;
        private UUID deptId;

        private void setDept(NewDepartmentEntity dept) {
            this.dept = dept;
            this.deptId = dept.getId();
        }

        protected List<StuffDto> execMethod() {
            return stuffDao.getListByDepartment(deptId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setDept(randomDept());

            List<NewStuffEntity> targets = new ArrayList<>();
            for(NewStuffEntity dept : stub.ALL_STUFFS) {
                if(dept.getDepartmentId().equals(deptId)) targets.add(dept);
            }

            when(stuffRepository.findByDepartmentId(deptId)).thenReturn(targets);
            TestHelper.listCompareTest(this::execMethod, toStuffDtoList(targets));
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById {
        private NewStuffEntity stuff;
        private UUID stuffId;

        private void setStuff(NewStuffEntity stuff) {
            this.stuff = stuff;
            this.stuffId = stuff.getId();
        }

        protected StuffDto execMethod() {
            return stuffDao.getById(stuffId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setStuff(randomStuff());

            when(stuffRepository.findById(stuffId)).thenReturn(Optional.of(stuff));
            TestHelper.objectCompareTest(this::execMethod, stuff.toStuffDto());
        }

        @Test
        @DisplayName("[ERROR]_[`id`에 해당하는 `stuff`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setStuff(randomStuff());

            when(stuffRepository.findById(stuffId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private NewStuffEntity stuff;

        private void setStuff(NewStuffEntity stuff) {
            this.stuff = stuff;
        }

        protected StuffDto execMethod() {
            return stuffDao.create(stuff.getId(), stuff.getDepartmentId(), stuff.getName(), stuff.getThumbnail());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setStuff(randomStuff());

            when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(Optional.of(stuff.getDepartment()));
            when(stuffRepository.existsById(stuff.getId())).thenReturn(false);
            when(stuffRepository.existsByDepartmentIdAndName(stuff.getDepartmentId(), stuff.getName())).thenReturn(false);
            when(stuffRepository.save(mockArg(stuff))).thenReturn(stuff);

            TestHelper.objectCompareTest(this::execMethod, stuff.toStuffDto());

            verify(stuffRepository).save(mockArg(stuff));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`departmentId`에 해당하는 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_universityNotFound_NotFoundException() {
            setStuff(randomStuff());

            when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `id`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
        public void ERROR_idConflict_ConflictException() {
            setStuff(randomStuff());

            when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(Optional.of(stuff.getDepartment()));
            when(stuffRepository.existsById(stuff.getId())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setStuff(randomStuff());

            when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(Optional.of(stuff.getDepartment()));
            when(stuffRepository.existsById(stuff.getId())).thenReturn(false);
            when(stuffRepository.existsByDepartmentIdAndName(stuff.getDepartmentId(), stuff.getName())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        private NewStuffEntity target;
        private UUID targetId;
        private NewStuffEntity newStuff;

        private void setTarget(NewStuffEntity target) {
            this.target = target;
            this.targetId = target.getId();
        }

        private NewStuffEntity updatedStuff() {
            return target
                    .withDepartment(newStuff.getDepartment())
                    .withName(newStuff.getName())
                    .withThumbnail(newStuff.getThumbnail());
        }

        protected StuffDto execMethod() {
            return stuffDao.update(targetId, newStuff.getDepartmentId(), newStuff.getName(), newStuff.getThumbnail());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`index`를 변경하지 않을 시]_[-]")
        public void SUCCESS_notChangeIndex() {
            setTarget(randomStuff());
            newStuff = target
                    .withThumbnail("_");

            when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(Optional.of(newStuff.getDepartment()));
            when(stuffRepository.save(mockArg(updatedStuff()))).thenReturn(updatedStuff());

            TestHelper.objectCompareTest(this::execMethod, updatedStuff().toStuffDto());

            verify(stuffRepository).save(mockArg(updatedStuff()));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`index`를 변경할 시]_[-]")
        public void SUCCESS_changeIndex() {
            setTarget(randomStuff());
            newStuff = randomStuff()
                    .withDepartment(randomDept())
                    .withName("__NEW_NAME")
                    .withThumbnail("_");

            when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(Optional.of(newStuff.getDepartment()));
            when(stuffRepository.existsByDepartmentIdAndName(newStuff.getDepartmentId(), newStuff.getName())).thenReturn(false);
            when(stuffRepository.save(mockArg(updatedStuff()))).thenReturn(updatedStuff());

            TestHelper.objectCompareTest(this::execMethod, updatedStuff().toStuffDto());

            verify(stuffRepository).save(mockArg(updatedStuff()));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`id`에 해당하는 `stuff`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_majorNotFound_NotFoundException() {
            setTarget(randomStuff());
            newStuff = randomStuff()
                    .withDepartment(randomDept())
                    .withName("__NEW_NAME")
                    .withThumbnail("_");

            when(stuffRepository.findById(targetId)).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }


        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`departmentId`에 해당하는 `department`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_departmentNotFound_NotFoundException() {
            setTarget(randomStuff());
            newStuff = randomStuff()
                    .withDepartment(randomDept())
                    .withName("__NEW_NAME")
                    .withThumbnail("_");

            when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setTarget(randomStuff());
            newStuff = randomStuff()
                    .withDepartment(randomDept())
                    .withName("__NEW_NAME")
                    .withThumbnail("_");

            when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(Optional.of(newStuff.getDepartment()));
            when(stuffRepository.existsByDepartmentIdAndName(newStuff.getDepartmentId(), newStuff.getName())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }
}
