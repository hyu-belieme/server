package com.belieme.apiserver.data;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.belieme.apiserver.data.daoimpl.DepartmentDaoImpl;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.MajorDepartmentJoinEntity;
import com.belieme.apiserver.data.entity.MajorEntity;
import com.belieme.apiserver.data.entity.UniversityEntity;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.error.exception.ConflictException;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.RandomGetter;
import com.belieme.apiserver.util.TestHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepartmentDaoTest extends BaseDaoTest {

  @InjectMocks
  private DepartmentDaoImpl departmentDao;

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

    private UniversityEntity univ;
    private UUID univId;

    private void setUniv(UniversityEntity univ) {
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

      List<DepartmentEntity> targets = new ArrayList<>();
      for (DepartmentEntity dept : stub.ALL_DEPTS) {
        if (dept.getUniversity().getId().equals(univId)) {
          targets.add(dept);
        }
      }

      when(univRepository.existsById(univId)).thenReturn(true);
      when(deptRepository.findByUniversityId(univId)).thenReturn(targets);
      TestHelper.listCompareTest(this::execMethod, toDepartmentDtoList(targets));
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

    private DepartmentEntity dept;
    private UUID deptId;

    private void setDept(DepartmentEntity dept) {
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

    private DepartmentEntity dept;
    private UUID deptId;

    private void setDept(DepartmentEntity dept) {
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

    private DepartmentEntity dept;

    private void setDept(DepartmentEntity department) {
      this.dept = department;
    }

    protected DepartmentDto execMethod() {
      return departmentDao.create(dept.getId(), dept.getUniversityId(), dept.getName(),
          dept.getBaseMajorJoin().stream().map(MajorDepartmentJoinEntity::getMajorId).toList());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setDept(randomDept());

      when(univRepository.findById(dept.getUniversityId())).thenReturn(
          Optional.of(dept.getUniversity()));
      when(deptRepository.existsById(dept.getId())).thenReturn(false);
      when(deptRepository.existsByUniversityIdAndName(dept.getUniversityId(),
          dept.getName())).thenReturn(false);
      for (MajorDepartmentJoinEntity majorJoin : dept.getBaseMajorJoin()) {
        when(majorDeptJoinRepository.save(mockArg(majorJoin))).thenReturn(majorJoin);
      }
      when(deptRepository.save(mockArg(dept))).thenReturn(dept);

      TestHelper.objectCompareTest(this::execMethod, dept.toDepartmentDto());

      for (MajorDepartmentJoinEntity majorJoin : dept.getBaseMajorJoin()) {
        verify(majorDeptJoinRepository).save(majorJoin);
      }
      verify(deptRepository).save(dept);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `id`를 갖는 `department`가 존재할 시]_[ConflictException]")
    public void ERROR_idConflict_ConflictException() {
      setDept(randomDept());

      when(univRepository.findById(dept.getUniversityId())).thenReturn(
          Optional.of(dept.getUniversity()));
      when(deptRepository.existsById(dept.getId())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `department`가 존재할 시]_[ConflictException]")
    public void ERROR_indexConflict_ConflictException() {
      setDept(randomDept());

      when(univRepository.findById(dept.getUniversityId())).thenReturn(
          Optional.of(dept.getUniversity()));
      when(deptRepository.existsById(dept.getId())).thenReturn(false);
      when(deptRepository.existsByUniversityIdAndName(dept.getUniversityId(),
          dept.getName())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`index`에 해당하는 `university`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_universityNotExist_InvalidIndexException() {
      setDept(randomDept());

      when(univRepository.findById(dept.getUniversityId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }
  }

  @Nested
  @DisplayName("update()")
  public class TestUpdate {

    private DepartmentEntity target;
    private UUID targetId;
    private DepartmentEntity newDept;

    private void setTarget(DepartmentEntity dept) {
      this.target = dept;
      this.targetId = dept.getId();
    }

    private DepartmentEntity savedDept() {
      return target.withUniversity(newDept.getUniversity()).withName(newDept.getName())
          .withBaseMajor(newDept.getBaseMajorJoin());
    }

    protected DepartmentDto execMethod() {
      return departmentDao.update(targetId, newDept.getUniversityId(), newDept.getName(),
          newDept.getBaseMajorJoin().stream().map(MajorDepartmentJoinEntity::getMajorId).toList());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[index(name)을 변경하지 않을 시]_[-]")
    public void SUCCESS_notChangeIndex() {
      setTarget(randomDept());

      newDept = target;
      newDept = addNewMajor(newDept);
      newDept = addNewMajor(newDept);

      when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(univRepository.findById(newDept.getUniversityId())).thenReturn(
          Optional.of(newDept.getUniversity()));

      for (MajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
        when(majorRepository.findById(majorJoin.getMajorId())).thenReturn(
            Optional.of(majorJoin.getMajor()));
        when(majorDeptJoinRepository.save(mockArg(majorJoin))).thenReturn(majorJoin);
      }
      when(deptRepository.save(mockArg(savedDept()))).thenReturn(savedDept());

      TestHelper.objectCompareTest(this::execMethod, savedDept().toDepartmentDto());

      for (MajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
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
      when(univRepository.findById(newDept.getUniversityId())).thenReturn(
          Optional.of(newDept.getUniversity()));
      when(deptRepository.existsByUniversityIdAndName(newDept.getUniversityId(),
          newDept.getName())).thenReturn(false);

      for (MajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
        when(majorRepository.findById(majorJoin.getMajorId())).thenReturn(
            Optional.of(majorJoin.getMajor()));
        when(majorDeptJoinRepository.save(mockArg(majorJoin))).thenReturn(majorJoin);
      }
      when(deptRepository.save(mockArg(savedDept()))).thenReturn(savedDept());

      TestHelper.objectCompareTest(this::execMethod, savedDept().toDepartmentDto());

      for (MajorDepartmentJoinEntity majorJoin : newDept.getBaseMajorJoin()) {
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
    @DisplayName("[ERROR]_[`NewDept`의 `index`에 해당하는 `university`가 없을 시]_[InvalidIndexException]")
    public void ERROR_univOfNewDeptNotFound_InvalidIndexException() {
      setTarget(randomDept());

      newDept = target.withName("__NEW_NAME");
      newDept = addNewMajor(newDept);
      newDept = addNewMajor(newDept);

      when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(univRepository.findById(newDept.getUniversityId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`NewDept`의 `index`에 해당하는 `department`가 기존에 있을 시]_[ConflictException]")
    public void ERROR_newDeptConflict_ConflictException() {
      setTarget(randomDept());

      newDept = target.withName("__NEW_NAME");
      newDept = addNewMajor(newDept);
      newDept = addNewMajor(newDept);

      when(deptRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(univRepository.findById(newDept.getUniversityId())).thenReturn(
          Optional.of(newDept.getUniversity()));
      when(deptRepository.existsByUniversityIdAndName(newDept.getUniversityId(),
          newDept.getName())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    private DepartmentEntity addNewMajor(DepartmentEntity dept) {
      MajorEntity newMajor = randomMajorNotInDept(dept);
      if (newMajor == null) {
        return dept;
      }
      return dept.withBaseMajorAdd(new MajorDepartmentJoinEntity(newMajor, dept));
    }
  }

  private MajorEntity randomMajorNotInDept(DepartmentEntity dept) {
    return randomSelectAndLog(majorsNotInDept(allMajors(), dept));
  }

  private RandomGetter<MajorEntity> majorsNotInDept(RandomGetter<MajorEntity> majors,
      DepartmentEntity dept) {
    return majors.filter((major) -> major.getUniversityId().equals(dept.getUniversityId())
        && !doesDeptContainMajorJoin(dept, major));
  }

  private boolean doesDeptContainMajorJoin(DepartmentEntity dept, MajorEntity major) {
    for (MajorDepartmentJoinEntity majorJoin : dept.getBaseMajorJoin()) {
      if (majorJoin.getMajor().equals(major)) {
        return true;
      }
    }
    return false;
  }
}
