package com.belieme.apiserver.data.daoimpl;

import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.entity.UserEntity;
import com.belieme.apiserver.data.repository.AuthorityRepository;
import com.belieme.apiserver.data.repository.AuthorityUserJoinRepository;
import com.belieme.apiserver.data.repository.DepartmentRepository;
import com.belieme.apiserver.data.repository.HistoryRepository;
import com.belieme.apiserver.data.repository.ItemRepository;
import com.belieme.apiserver.data.repository.MajorDepartmentJoinRepository;
import com.belieme.apiserver.data.repository.MajorRepository;
import com.belieme.apiserver.data.repository.StuffRepository;
import com.belieme.apiserver.data.repository.UniversityRepository;
import com.belieme.apiserver.data.repository.UserRepository;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dto.HistoryCursorDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.enumeration.HistoryStatus;
import com.belieme.apiserver.error.exception.ConflictException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class HistoryDaoImpl extends BaseDaoImpl implements HistoryDao {

  public HistoryDaoImpl(UniversityRepository universityRepository,
      DepartmentRepository departmentRepository, UserRepository userRepository,
      MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository,
      AuthorityRepository authorityRepository,
      AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository,
      ItemRepository itemRepository, HistoryRepository historyRepository) {
    super(universityRepository, departmentRepository, userRepository, majorRepository,
        majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository,
        stuffRepository, itemRepository, historyRepository);
  }

  @Override
  public List<HistoryDto> getAllList() {
    return toHistoryDtoList(historyRepository.findAll());
  }

  @Override
  public List<HistoryDto> getListByDepartment(@NonNull UUID departmentId,
      HistoryStatus status) {
    validateDepartmentId(departmentId);

    List<HistoryEntity> targetList = historyRepository.findByDepartmentId(departmentId);
    return filterByStatus(targetList, status);
  }

  @Override
  public List<HistoryDto> getListByDepartment(@NonNull UUID departmentId,
      HistoryStatus status, HistoryCursorDto cursor, int limit) {
    validateDepartmentId(departmentId);

    List<HistoryEntity> targetList = historyRepository.findByDepartmentId(departmentId);
    return filterByStatusAndCursor(targetList, status, cursor, limit);
  }

  @Override
  public List<HistoryDto> getListByDepartmentAndRequester(
      @NonNull UUID departmentId, @NonNull UUID requesterId,
      HistoryStatus status) {
    validateDepartmentId(departmentId);
    validateUserId(requesterId);

    List<HistoryEntity> targetList = historyRepository.findByDepartmentIdAndRequesterId(departmentId, requesterId);
    return filterByStatus(targetList, status);
  }

  @Override
  public List<HistoryDto> getListByDepartmentAndRequester(
      @NonNull UUID departmentId, @NonNull UUID requesterId,
      HistoryStatus status, HistoryCursorDto cursor, int limit) {
    validateDepartmentId(departmentId);
    validateUserId(requesterId);

    List<HistoryEntity> targetList = historyRepository.findByDepartmentIdAndRequesterId(departmentId, requesterId);
    return filterByStatusAndCursor(targetList, status, cursor, limit);
  }

  @Override
  public HistoryDto getById(@NonNull UUID historyId) {
    return findHistoryEntity(historyId).toHistoryDto();
  }

  @Override
  public HistoryDto create(@NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
      UUID approveManagerId, UUID returnManagerId, UUID lostManagerId, UUID cancelManagerId,
      long requestedAt, long approvedAt, long returnedAt, long lostAt, long canceledAt) {
    ItemEntity itemOfNewHistory = getItemEntityOrThrowInvalidIndexException(itemId);

    checkHistoryIdConflict(historyId);
    checkHistoryConflict(itemId, num);

    HistoryEntity newHistoryEntity = new HistoryEntity(historyId, itemOfNewHistory, num,
        getUserEntityIfIdIsNotNull(requesterId), getUserEntityIfIdIsNotNull(approveManagerId),
        getUserEntityIfIdIsNotNull(returnManagerId), getUserEntityIfIdIsNotNull(lostManagerId),
        getUserEntityIfIdIsNotNull(cancelManagerId), requestedAt, approvedAt, returnedAt, lostAt,
        canceledAt);
    return historyRepository.save(newHistoryEntity).toHistoryDto();
  }

  @Override
  public HistoryDto update(@NonNull UUID historyId, @NonNull UUID itemId, int num, UUID requesterId,
      UUID approveManagerId, UUID returnManagerId, UUID lostManagerId, UUID cancelManagerId,
      long requestedAt, long approvedAt, long returnedAt, long lostAt, long canceledAt) {
    HistoryEntity target = findHistoryEntity(historyId);
    ItemEntity itemOfNewHistory = getItemEntityOrThrowInvalidIndexException(itemId);

    if (doesIndexChange(target, itemId, num)) {
      checkHistoryConflict(itemOfNewHistory.getId(), num);
    }

    target = target.withItem(itemOfNewHistory).withNum(num)
        .withRequester(getUserEntityIfIdIsNotNull(requesterId))
        .withApproveManager(getUserEntityIfIdIsNotNull(approveManagerId))
        .withReturnManager(getUserEntityIfIdIsNotNull(returnManagerId))
        .withLostManager(getUserEntityIfIdIsNotNull(lostManagerId))
        .withCancelManager(getUserEntityIfIdIsNotNull(cancelManagerId)).withRequestedAt(requestedAt)
        .withApprovedAt(approvedAt).withReturnedAt(returnedAt).withLostAt(lostAt)
        .withCanceledAt(canceledAt);
    return historyRepository.save(target).toHistoryDto();
  }

  private List<HistoryDto> filterByStatus(List<HistoryEntity> targetList,
      HistoryStatus status) {
    List<HistoryDto> output = new ArrayList<>();
    for (HistoryEntity historyEntity : targetList) {
      HistoryDto history = historyEntity.toHistoryDto();
      if(status != null && !history.status().dividedTo(status)) continue;
      output.add(history);
    }
    return output;
  }

  private List<HistoryDto> filterByStatusAndCursor(
      List<HistoryEntity> targetList, HistoryStatus status, HistoryCursorDto cursor, int limit) {
    List<HistoryDto> output = new ArrayList<>();
    for (HistoryEntity historyEntity : targetList) {
      if (output.size() >= limit) break;

      HistoryDto history = historyEntity.toHistoryDto();
      if (status != null && !history.status().dividedTo(status)) continue;

      if (cursor != null && history.requestedAt() > cursor.getRequestedAt()) continue;
      if (cursor != null && history.requestedAt() < cursor.getRequestedAt()) {
        output.add(history);
        continue;
      }

      if (cursor != null && history.lostAt() > cursor.getLostAt()) continue;
      if (cursor != null && history.lostAt() < cursor.getLostAt()) {
        output.add(history);
        continue;
      }

      if (cursor != null && history.id().compareTo(cursor.getId()) < 0) continue;
      output.add(history);
    }
    return output;
  }

  private List<HistoryDto> toHistoryDtoList(Iterable<HistoryEntity> historyEntities) {
    ArrayList<HistoryDto> output = new ArrayList<>();

    for (HistoryEntity historyEntity : historyEntities) {
      output.add(historyEntity.toHistoryDto());
    }
    return output;
  }

  private UserEntity getUserEntityIfIdIsNotNull(UUID userId) {
    if (userId == null) {
      return null;
    }
    return getUserEntityOrThrowInvalidIndexException(userId);
  }

  private boolean doesIndexChange(HistoryEntity target, UUID newItemId, int newHistoryNum) {
    return !(target.getItemId().equals(newItemId) && target.getNum() == newHistoryNum);
  }

  private void checkHistoryIdConflict(UUID historyId) {
    if (historyRepository.existsById(historyId)) {
      throw new ConflictException();
    }
  }

  private void checkHistoryConflict(UUID itemId, int historyNum) {
    if (historyRepository.existsByItemIdAndNum(itemId, historyNum)) {
      throw new ConflictException();
    }
  }
}
