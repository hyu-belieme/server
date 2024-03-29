package com.belieme.apiserver.data;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.belieme.apiserver.data.daoimpl.HistoryDaoImpl;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.UserEntity;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.enumeration.HistoryStatus;
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
public class HistoryDaoTest extends BaseDaoTest {

  @InjectMocks
  private HistoryDaoImpl historyDao;

  @Nested
  @DisplayName("getAllList()")
  public class TestGetAllList {

    protected List<HistoryDto> execMethod() {
      return historyDao.getAllList();
    }

    @Test()
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      when(historyRepository.findAll()).thenReturn(stub.ALL_HISTORIES);
      TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(stub.ALL_HISTORIES));
    }
  }

  @Nested
  @DisplayName("getListByDepartment()")
  public class TestGetListByDepartment {

    private DepartmentEntity dept;
    private UUID deptId;

    private HistoryStatus status;

    private void setDept(DepartmentEntity dept) {
      this.dept = dept;
      this.deptId = dept.getId();
    }

    private void setStatus(HistoryStatus status) {
      this.status = status;
    }

    protected List<HistoryDto> execMethod() {
      return historyDao.getListByDepartment(deptId, status);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setDept(randomDept());
      setStatus(randomHistoryStatus());

      when(deptRepository.existsById(deptId)).thenReturn(true);
      when(historyRepository.findByDepartmentId(deptId)).thenReturn(historiesByDepartment(deptId));

      List<HistoryEntity> expect = new ArrayList<>();
      for (HistoryEntity history : stub.ALL_HISTORIES) {
        HistoryDto historyDto = history.toHistoryDto();
        if (history.getItem().getStuff().getDepartmentId().equals(deptId)
            && (this.status == null || historyDto.status().dividedTo(this.status))) {
          expect.add(history);
        }
      }
      TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(expect));
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
  @DisplayName("getListByDepartmentAndRequester()")
  public class TestGetListByDepartmentAndRequester {

    private UserEntity requester;
    private UUID requesterId;

    private DepartmentEntity dept;
    private UUID deptId;

    private HistoryStatus status;

    private void setUser(UserEntity requester) {
      this.requester = requester;
      this.requesterId = requester.getId();
    }

    private void setDept(DepartmentEntity dept) {
      this.dept = dept;
      this.deptId = dept.getId();
    }

    private void setStatus(HistoryStatus status) {
      this.status = status;
    }

    protected List<HistoryDto> execMethod() {
      return historyDao.getListByDepartmentAndRequester(deptId, requesterId, status);
    }

    @RepeatedTest(20)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setDept(randomDept());
      setUser(randomUser());
      setStatus(randomHistoryStatus());

      when(deptRepository.existsById(deptId)).thenReturn(true);
      when(userRepository.existsById(requesterId)).thenReturn(true);
      when(historyRepository.findByDepartmentIdAndRequesterId(deptId, requesterId)).thenReturn(
          historiesByDepartmentAndRequester(deptId, requesterId));

      List<HistoryEntity> expect = new ArrayList<>();
      for (HistoryEntity history : stub.ALL_HISTORIES) {
        HistoryDto historyDto = history.toHistoryDto();
        if (history.getItem().getStuff().getDepartmentId().equals(deptId)
            && history.getRequesterId() != null && history.getRequesterId().equals(requesterId)
            && (this.status == null || historyDto.status().dividedTo(this.status))) {
          expect.add(history);
        }
      }
      TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(expect));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[잘못된 `departmentId`가 주어졌을 시]_[InvalidIndexException]")
    public void ERROR_wrongDepartmentId_invalidIndexException() {
      setDept(randomDept());
      setUser(randomUser());

      when(deptRepository.existsById(deptId)).thenReturn(false);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[잘못된 `userId`가 주어졌을 시]_[InvalidIndexException]")
    public void ERROR_wrongUserId_invalidIndexException() {
      setDept(randomDept());
      setUser(randomUser());

      when(deptRepository.existsById(deptId)).thenReturn(true);
      when(userRepository.existsById(requesterId)).thenReturn(false);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }
  }

  @Nested
  @DisplayName("getById()")
  public class TestGetById {

    private HistoryEntity history;
    private UUID historyId;

    private void setHistory(HistoryEntity history) {
      this.history = history;
      this.historyId = history.getId();
    }

    protected HistoryDto execMethod() {
      return historyDao.getById(historyId);
    }

    @Test
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setHistory(randomHistory());

      when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
      TestHelper.objectCompareTest(this::execMethod, history.toHistoryDto());
    }

    @Test
    @DisplayName("[ERROR]_[`id`에 해당하는 `history`가 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_notFound_NotFoundException() {
      setHistory(randomHistory());

      when(historyRepository.findById(historyId)).thenReturn(Optional.empty());
      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }
  }

  @Nested
  @DisplayName("create()")
  public class TestCreate {

    private HistoryEntity history;

    private void setHistory(HistoryEntity history) {
      this.history = history;
    }

    protected HistoryDto execMethod() {
      return historyDao.create(history.getId(), history.getItemId(), history.getNum(),
          history.getRequesterId(), history.getApproveManagerId(), history.getReturnManagerId(),
          history.getLostManagerId(), history.getCancelManagerId(), history.getRequestedAt(),
          history.getApprovedAt(), history.getReturnedAt(), history.getLostAt(),
          history.getCanceledAt());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setHistory(randomHistory());

      when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
      when(historyRepository.existsById(history.getId())).thenReturn(false);
      when(
          historyRepository.existsByItemIdAndNum(history.getItemId(), history.getNum())).thenReturn(
          false);
      if (history.getRequester() != null) {
        when(userRepository.findById(history.getRequesterId())).thenReturn(
            Optional.of(history.getRequester()));
      }
      if (history.getApproveManager() != null) {
        when(userRepository.findById(history.getApproveManagerId())).thenReturn(
            Optional.of(history.getApproveManager()));
      }
      if (history.getReturnManager() != null) {
        when(userRepository.findById(history.getReturnManagerId())).thenReturn(
            Optional.of(history.getReturnManager()));
      }
      if (history.getLostManager() != null) {
        when(userRepository.findById(history.getLostManagerId())).thenReturn(
            Optional.of(history.getLostManager()));
      }
      if (history.getCancelManager() != null) {
        when(userRepository.findById(history.getCancelManagerId())).thenReturn(
            Optional.of(history.getCancelManager()));
      }

      when(historyRepository.save(mockArg(history))).thenReturn(history);

      TestHelper.objectCompareTest(this::execMethod, history.toHistoryDto());

      verify(historyRepository).save(mockArg(history));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`itemId`에 해당하는 `item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_universityNotFound_InvalidIndexException() {
      setHistory(randomHistory());

      when(itemRepository.findById(history.getItemId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `id`를 갖는 `history`가 존재할 시]_[ConflictException]")
    public void ERROR_IdConflict_ConflictException() {
      setHistory(randomHistory());

      when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
      when(historyRepository.existsById(history.getId())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `history`가 존재할 시]_[ConflictException]")
    public void ERROR_IndexConflict_ConflictException() {
      setHistory(randomHistory());

      when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
      when(historyRepository.existsById(history.getId())).thenReturn(false);
      when(
          historyRepository.existsByItemIdAndNum(history.getItemId(), history.getNum())).thenReturn(
          true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`userId`에 해당하는 `user`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_wrongUnivId_InvalidIndexException() {
      setHistory(randomHistory());

      when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
      when(
          historyRepository.existsByItemIdAndNum(history.getItemId(), history.getNum())).thenReturn(
          false);
      when(userRepository.findById(any())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }
  }

  @Nested
  @DisplayName("update()")
  public class TestUpdate {

    private HistoryEntity target;
    private UUID targetId;
    private HistoryEntity newHistory;

    private void setTarget(HistoryEntity target) {
      this.target = target;
      this.targetId = target.getId();
    }

    private HistoryEntity updatedHistory() {
      return target.withItem(newHistory.getItem()).withNum(newHistory.getNum())
          .withRequester(newHistory.getRequester())
          .withApproveManager(newHistory.getApproveManager())
          .withReturnManager(newHistory.getReturnManager())
          .withLostManager(newHistory.getLostManager())
          .withCancelManager(newHistory.getCancelManager())
          .withRequestedAt(newHistory.getRequestedAt()).withApprovedAt(newHistory.getApprovedAt())
          .withReturnedAt(newHistory.getReturnedAt()).withLostAt(newHistory.getLostAt())
          .withCanceledAt(newHistory.getCanceledAt());
    }

    protected HistoryDto execMethod() {
      return historyDao.update(targetId, newHistory.getItemId(), newHistory.getNum(),
          newHistory.getRequesterId(), newHistory.getApproveManagerId(),
          newHistory.getReturnManagerId(), newHistory.getLostManagerId(),
          newHistory.getCancelManagerId(), newHistory.getRequestedAt(), newHistory.getApprovedAt(),
          newHistory.getReturnedAt(), newHistory.getLostAt(), newHistory.getCanceledAt());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`index`를 변경하지 않을 시]_[-]")
    public void SUCCESS_notChangeIndex() {
      setTarget(randomHistory());
      newHistory = target.withApproveManager(randomUser()).withApprovedAt(1683076516);

      when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(itemRepository.findById(newHistory.getItemId())).thenReturn(
          Optional.of(newHistory.getItem()));

      if (newHistory.getRequester() != null) {
        when(userRepository.findById(newHistory.getRequesterId())).thenReturn(
            Optional.of(newHistory.getRequester()));
      }
      if (newHistory.getApproveManager() != null) {
        when(userRepository.findById(newHistory.getApproveManagerId())).thenReturn(
            Optional.of(newHistory.getApproveManager()));
      }
      if (newHistory.getReturnManager() != null) {
        when(userRepository.findById(newHistory.getReturnManagerId())).thenReturn(
            Optional.of(newHistory.getReturnManager()));
      }
      if (newHistory.getLostManager() != null) {
        when(userRepository.findById(newHistory.getLostManagerId())).thenReturn(
            Optional.of(newHistory.getLostManager()));
      }
      if (newHistory.getCancelManager() != null) {
        when(userRepository.findById(newHistory.getCancelManagerId())).thenReturn(
            Optional.of(newHistory.getCancelManager()));
      }

      when(historyRepository.save(mockArg(updatedHistory()))).thenReturn(updatedHistory());

      TestHelper.objectCompareTest(this::execMethod, updatedHistory().toHistoryDto());

      verify(historyRepository).save(mockArg(updatedHistory()));
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`index`를 변경할 시]_[-]")
    public void SUCCESS_changeIndex() {
      setTarget(randomHistory());
      newHistory = randomHistory().withItem(randomItem()).withNum(9000)
          .withReturnManager(randomUser()).withReturnedAt(1683076516);

      when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(itemRepository.findById(newHistory.getItemId())).thenReturn(
          Optional.of(newHistory.getItem()));
      when(historyRepository.existsByItemIdAndNum(newHistory.getItemId(),
          newHistory.getNum())).thenReturn(false);

      if (newHistory.getRequester() != null) {
        when(userRepository.findById(newHistory.getRequesterId())).thenReturn(
            Optional.of(newHistory.getRequester()));
      }
      if (newHistory.getApproveManager() != null) {
        when(userRepository.findById(newHistory.getApproveManagerId())).thenReturn(
            Optional.of(newHistory.getApproveManager()));
      }
      if (newHistory.getReturnManager() != null) {
        when(userRepository.findById(newHistory.getReturnManagerId())).thenReturn(
            Optional.of(newHistory.getReturnManager()));
      }
      if (newHistory.getLostManager() != null) {
        when(userRepository.findById(newHistory.getLostManagerId())).thenReturn(
            Optional.of(newHistory.getLostManager()));
      }
      if (newHistory.getCancelManager() != null) {
        when(userRepository.findById(newHistory.getCancelManagerId())).thenReturn(
            Optional.of(newHistory.getCancelManager()));
      }

      when(historyRepository.save(mockArg(updatedHistory()))).thenReturn(updatedHistory());

      TestHelper.objectCompareTest(this::execMethod, updatedHistory().toHistoryDto());

      verify(historyRepository).save(mockArg(updatedHistory()));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`id`에 해당하는 `history`가 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_majorNotFound_NotFoundException() {
      setTarget(randomHistory());
      newHistory = randomHistory().withItem(randomItem()).withNum(9000)
          .withReturnManager(randomUser()).withReturnedAt(1683076516);

      when(historyRepository.findById(targetId)).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }


    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`itemId`에 해당하는 `item`이 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_itemNotFound_InvalidIndexException() {
      setTarget(randomHistory());
      newHistory = randomHistory().withItem(randomItem()).withNum(9000)
          .withReturnManager(randomUser()).withReturnedAt(1683076516);

      when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(itemRepository.findById(newHistory.getItemId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`userId`에 해당하는 `user`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_wrongUnivId_InvalidIndexException() {
      setTarget(randomHistory());
      newHistory = randomHistory().withItem(randomItem()).withNum(9000)
          .withReturnManager(randomUser()).withReturnedAt(1683076516);

      when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(itemRepository.findById(newHistory.getItemId())).thenReturn(
          Optional.of(newHistory.getItem()));
      when(historyRepository.existsByItemIdAndNum(newHistory.getItemId(),
          newHistory.getNum())).thenReturn(false);
      when(userRepository.findById(any())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `history`가 존재할 시]_[ConflictException]")
    public void ERROR_IndexConflict_ConflictException() {
      setTarget(randomHistory());
      newHistory = randomHistory().withItem(randomItem()).withNum(9000)
          .withReturnManager(randomUser()).withReturnedAt(1683076516);

      when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(itemRepository.findById(newHistory.getItemId())).thenReturn(
          Optional.of(newHistory.getItem()));
      when(historyRepository.existsByItemIdAndNum(newHistory.getItemId(),
          newHistory.getNum())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }
  }

  private HistoryStatus randomHistoryStatus() {
    HistoryStatus[] statusList = HistoryStatus.values();
    int idx = (int) ((statusList.length + 1) * Math.random());

    if (idx >= statusList.length) return null;
    return statusList[(int) (statusList.length * Math.random())];
  }

  private List<HistoryEntity> historiesByDepartment(UUID deptId) {
    return stub.ALL_HISTORIES.stream()
        .filter((history) -> deptId.equals(history.getItem().getStuff().getDepartmentId()))
        .toList();
  }

  private List<HistoryEntity> historiesByDepartmentAndRequester(UUID deptId, UUID requesterId) {
    return stub.ALL_HISTORIES.stream()
        .filter((history) -> deptId.equals(history.getItem().getStuff().getDepartmentId()))
        .filter((history) -> requesterId.equals(history.getRequesterId()))
        .toList();
  }
}