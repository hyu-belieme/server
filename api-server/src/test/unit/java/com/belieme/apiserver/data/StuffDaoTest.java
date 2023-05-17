package com.belieme.apiserver.data;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.belieme.apiserver.data.daoimpl.StuffDaoImpl;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.error.exception.ConflictException;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
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
public class StuffDaoTest extends BaseDaoTest {

  @InjectMocks
  private StuffDaoImpl stuffDao;

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

    private DepartmentEntity dept;
    private UUID deptId;

    private void setDept(DepartmentEntity dept) {
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

      List<StuffEntity> targets = new ArrayList<>();
      for (StuffEntity dept : stub.ALL_STUFFS) {
        if (dept.getDepartmentId().equals(deptId)) {
          targets.add(dept);
        }
      }

      when(deptRepository.existsById(deptId)).thenReturn(true);
      when(stuffRepository.findByDepartmentId(deptId)).thenReturn(targets);
      TestHelper.listCompareTest(this::execMethod, toStuffDtoList(targets));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[잘못된 `departmentId`가 주어졌을 시]_[InvalidIndexException]")
    public void ERROR_wrongDepartmentId_invalidIndexException() {
      setDept(randomDept());

      when(deptRepository.existsById(deptId)).thenReturn(false);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }
  }

  @Nested
  @DisplayName("getById()")
  public class TestGetById {

    private StuffEntity stuff;
    private UUID stuffId;

    private void setStuff(StuffEntity stuff) {
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

    private StuffEntity stuff;

    private void setStuff(StuffEntity stuff) {
      this.stuff = stuff;
    }

    protected StuffDto execMethod() {
      return stuffDao.create(stuff.getId(), stuff.getDepartmentId(), stuff.getName(),
          stuff.getThumbnail());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setStuff(randomStuff());

      when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(
          Optional.of(stuff.getDepartment()));
      when(stuffRepository.existsById(stuff.getId())).thenReturn(false);
      when(stuffRepository.existsByDepartmentIdAndName(stuff.getDepartmentId(),
          stuff.getName())).thenReturn(false);
      when(stuffRepository.save(mockArg(stuff))).thenReturn(stuff);

      TestHelper.objectCompareTest(this::execMethod, stuff.toStuffDto());

      verify(stuffRepository).save(mockArg(stuff));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`departmentId`에 해당하는 `department`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_universityNotFound_InvalidIndexException() {
      setStuff(randomStuff());

      when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `id`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
    public void ERROR_idConflict_ConflictException() {
      setStuff(randomStuff());

      when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(
          Optional.of(stuff.getDepartment()));
      when(stuffRepository.existsById(stuff.getId())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
    public void ERROR_IndexConflict_ConflictException() {
      setStuff(randomStuff());

      when(deptRepository.findById(stuff.getDepartmentId())).thenReturn(
          Optional.of(stuff.getDepartment()));
      when(stuffRepository.existsById(stuff.getId())).thenReturn(false);
      when(stuffRepository.existsByDepartmentIdAndName(stuff.getDepartmentId(),
          stuff.getName())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }
  }

  @Nested
  @DisplayName("update()")
  public class TestUpdate {

    private StuffEntity target;
    private UUID targetId;
    private StuffEntity newStuff;

    private void setTarget(StuffEntity target) {
      this.target = target;
      this.targetId = target.getId();
    }

    private StuffEntity updatedStuff() {
      return target.withDepartment(newStuff.getDepartment()).withName(newStuff.getName())
          .withThumbnail(newStuff.getThumbnail());
    }

    protected StuffDto execMethod() {
      return stuffDao.update(targetId, newStuff.getDepartmentId(), newStuff.getName(),
          newStuff.getThumbnail());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`index`를 변경하지 않을 시]_[-]")
    public void SUCCESS_notChangeIndex() {
      setTarget(randomStuff());
      newStuff = target.withThumbnail("_");

      when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(
          Optional.of(newStuff.getDepartment()));
      when(stuffRepository.save(mockArg(updatedStuff()))).thenReturn(updatedStuff());

      TestHelper.objectCompareTest(this::execMethod, updatedStuff().toStuffDto());

      verify(stuffRepository).save(mockArg(updatedStuff()));
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`index`를 변경할 시]_[-]")
    public void SUCCESS_changeIndex() {
      setTarget(randomStuff());
      newStuff = randomStuff().withDepartment(randomDept()).withName("__NEW_NAME")
          .withThumbnail("_");

      when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(
          Optional.of(newStuff.getDepartment()));
      when(stuffRepository.existsByDepartmentIdAndName(newStuff.getDepartmentId(),
          newStuff.getName())).thenReturn(false);
      when(stuffRepository.save(mockArg(updatedStuff()))).thenReturn(updatedStuff());

      TestHelper.objectCompareTest(this::execMethod, updatedStuff().toStuffDto());

      verify(stuffRepository).save(mockArg(updatedStuff()));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`id`에 해당하는 `stuff`가 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_majorNotFound_NotFoundException() {
      setTarget(randomStuff());
      newStuff = randomStuff().withDepartment(randomDept()).withName("__NEW_NAME")
          .withThumbnail("_");

      when(stuffRepository.findById(targetId)).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }


    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`departmentId`에 해당하는 `department`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_departmentNotFound_InvalidIndexException() {
      setTarget(randomStuff());
      newStuff = randomStuff().withDepartment(randomDept()).withName("__NEW_NAME")
          .withThumbnail("_");

      when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
    public void ERROR_IndexConflict_ConflictException() {
      setTarget(randomStuff());
      newStuff = randomStuff().withDepartment(randomDept()).withName("__NEW_NAME")
          .withThumbnail("_");

      when(stuffRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(deptRepository.findById(newStuff.getDepartmentId())).thenReturn(
          Optional.of(newStuff.getDepartment()));
      when(stuffRepository.existsByDepartmentIdAndName(newStuff.getDepartmentId(),
          newStuff.getName())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }
  }
}
