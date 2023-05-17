package com.belieme.apiserver.domain.service;

import com.belieme.apiserver.config.initdata.InitialDataConfig;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dao.DepartmentDao;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dao.MajorDao;
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dao.UserDao;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.HistoryStatus;
import com.belieme.apiserver.domain.dto.enumeration.ItemStatus;
import com.belieme.apiserver.domain.exception.LostRegistrationRequestedOnLostItemException;
import com.belieme.apiserver.domain.exception.RentalCountLimitExceededException;
import com.belieme.apiserver.domain.exception.RentalCountOnSameStuffLimitExceededException;
import com.belieme.apiserver.domain.exception.ReservationRequestedOnNonUsableItemException;
import com.belieme.apiserver.domain.exception.RespondedOnUnrequestedItemException;
import com.belieme.apiserver.domain.exception.ReturnRegistrationRequestedOnReturnedItemException;
import com.belieme.apiserver.domain.exception.UsableItemNotExistedException;
import com.belieme.apiserver.domain.util.Constants;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class HistoryService extends BaseService {

  public HistoryService(InitialDataConfig initialData, UniversityDao universityDao,
      DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao,
      StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
    super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao,
        itemDao, historyDao);
  }

  public List<HistoryDto> getListByDepartment(@NonNull String userToken,
      @NonNull UUID departmentId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
    checkStaffPermission(requester, department);
    return historyDao.getListByDepartment(departmentId);
  }

  public List<HistoryDto> getListByStuff(@NonNull String userToken, @NonNull UUID stuffId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
    checkStaffPermission(requester, stuff.department());
    return historyDao.getListByStuff(stuffId);
  }

  public List<HistoryDto> getListByItem(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    ItemDto item = getItemOrThrowInvalidIndexException(itemId);
    checkStaffPermission(requester, item.stuff().department());
    return historyDao.getListByItem(itemId);
  }

  public List<HistoryDto> getListByDepartmentAndRequester(@NonNull String userToken,
      @NonNull UUID departmentId, @NonNull UUID userId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
    UserDto historyRequester = getUserOrThrowInvalidIndexException(userId);

    if (!requester.matchId(historyRequester)) {
      checkStaffPermission(requester, department);
    }
    checkUserPermission(requester, department);

    return historyDao.getListByDepartmentAndRequester(departmentId, userId);
  }

  public HistoryDto getById(@NonNull String userToken, @NonNull UUID historyId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    HistoryDto history = historyDao.getById(historyId);
    if (!requester.matchId(history.requester())) {
      checkStaffPermission(requester, history.item().stuff().department());
    }
    checkUserPermission(requester, history.item().stuff().department());
    return history;
  }

  public HistoryDto createReservationOnStuff(@NonNull String userToken, @NonNull UUID stuffId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
    DepartmentDto department = stuff.department();
    checkUserPermission(requester, department);
    checkRequesterRentalList(stuff, requester);

    ItemDto item = stuff.firstUsableItem();
    if (item == null) {
      throw new UsableItemNotExistedException();
    }
    if (item.isUnusable()) {
      throw new ReservationRequestedOnNonUsableItemException();
    }

    HistoryDto newHistory = createRentalHistory(item.id(), item.nextHistoryNum(), requester.id());
    ItemDto newItem = itemDao.update(item.id(), item.stuff().id(), item.num(), newHistory.id());

    return newHistory.withItem(newItem);
  }

  public HistoryDto createReservationOnItem(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    ItemDto item = getItemOrThrowInvalidIndexException(itemId);
    DepartmentDto department = item.stuff().department();
    checkUserPermission(requester, department);
    checkRequesterRentalList(item.stuff(), requester);

    if (item.isUnusable()) {
      throw new ReservationRequestedOnNonUsableItemException();
    }

    HistoryDto newHistory = createRentalHistory(item.id(), item.nextHistoryNum(), requester.id());
    ItemDto newItem = itemDao.update(item.id(), item.stuff().id(), item.num(), newHistory.id());

    return newHistory.withItem(newItem);
  }

  public HistoryDto makeItemLost(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    ItemDto item = getItemOrThrowInvalidIndexException(itemId);
    DepartmentDto department = item.stuff().department();
    checkStaffPermission(requester, department);

    if (item.status() == ItemStatus.LOST) {
      throw new LostRegistrationRequestedOnLostItemException();
    }

    HistoryDto newHistory;
    if (item.isUsable() || item.status() == ItemStatus.REQUESTED) {
      if (item.isUnusable() && item.lastHistory().status() == HistoryStatus.REQUESTED) {
        makeItemCancel(userToken, itemId);
      }
      newHistory = createLostHistory(item.id(), item.nextHistoryNum(), requester.id());
      ItemDto newItem = itemDao.update(item.id(), item.stuff().id(), item.num(), newHistory.id());

      return newHistory.withItem(newItem);
    }

    newHistory = item.lastHistory().withItem(item).withLostManager(requester)
        .withLostAt(currentTime());
    return updateHistory(newHistory);
  }

  public HistoryDto makeItemUsing(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    ItemDto item = getItemOrThrowInvalidIndexException(itemId);
    DepartmentDto department = item.stuff().department();
    checkStaffPermission(requester, department);

    HistoryDto lastHistory = item.lastHistory();
    if (lastHistory == null || lastHistory.status() != HistoryStatus.REQUESTED) {
      throw new RespondedOnUnrequestedItemException();
    }

    HistoryDto newHistory = lastHistory.withItem(item).withApproveManager(requester)
        .withApprovedAt(currentTime());
    return updateHistory(newHistory);
  }

  public HistoryDto makeItemReturn(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    ItemDto item = getItemOrThrowInvalidIndexException(itemId);
    DepartmentDto department = item.stuff().department();
    checkStaffPermission(requester, department);

    HistoryDto lastHistory = item.lastHistory();
    if (lastHistory == null || (lastHistory.status() != HistoryStatus.USING
        && lastHistory.status() != HistoryStatus.DELAYED
        && lastHistory.status() != HistoryStatus.LOST)) {
      throw new ReturnRegistrationRequestedOnReturnedItemException();
    }

    HistoryDto newHistory = lastHistory.withItem(item).withReturnManager(requester)
        .withReturnedAt(currentTime());
    return updateHistory(newHistory);
  }

  public HistoryDto makeItemCancel(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);

    ItemDto item = getItemOrThrowInvalidIndexException(itemId);
    DepartmentDto department = item.stuff().department();
    checkStaffPermission(requester, department);

    HistoryDto lastHistory = item.lastHistory();
    if (lastHistory == null || lastHistory.status() != HistoryStatus.REQUESTED) {
      throw new RespondedOnUnrequestedItemException();
    }

    HistoryDto newHistory = lastHistory.withItem(item).withCancelManager(requester)
        .withCanceledAt(currentTime());
    return updateHistory(newHistory);
  }

  private void checkRequesterRentalList(StuffDto stuff, UserDto requester) {
    List<HistoryDto> requesterHistory = historyDao.getListByDepartmentAndRequester(
        stuff.department().id(), requester.id());

    int usingItemCount = 0;
    int usingSameStuffCount = 0;
    for (HistoryDto history : requesterHistory) {
      if (history.status().isClosed()) {
        continue;
      }
      if (history.item().stuff().matchId(stuff)) {
        usingSameStuffCount += 1;
      }
      usingItemCount += 1;

      if (usingItemCount >= Constants.MAX_RENTAL_COUNT) {
        throw new RentalCountLimitExceededException();
      }
      if (usingSameStuffCount >= Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF) {
        throw new RentalCountOnSameStuffLimitExceededException();
      }
    }
  }

  private HistoryDto createRentalHistory(UUID itemId, int num, UUID requesterId) {
    return historyDao.create(UUID.randomUUID(), itemId, num, requesterId, null, null, null, null,
        currentTime(), 0, 0, 0, 0);
  }

  private HistoryDto createLostHistory(UUID itemId, int num, UUID lostManagerId) {
    return historyDao.create(UUID.randomUUID(), itemId, num, null, null, null, lostManagerId, null,
        0, 0, 0, currentTime(), 0);
  }

  private HistoryDto updateHistory(HistoryDto newHistory) {
    return historyDao.update(newHistory.id(), newHistory.item().id(), newHistory.num(),
        getUserIdOrNull(newHistory.requester()), getUserIdOrNull(newHistory.approveManager()),
        getUserIdOrNull(newHistory.returnManager()), getUserIdOrNull(newHistory.lostManager()),
        getUserIdOrNull(newHistory.cancelManager()), newHistory.requestedAt(),
        newHistory.approvedAt(), newHistory.returnedAt(), newHistory.lostAt(),
        newHistory.canceledAt());
  }

  private UUID getUserIdOrNull(UserDto user) {
    if (user == null) {
      return null;
    }
    return user.id();
  }

  private UserDto getUserOrThrowInvalidIndexException(UUID userId) {
    try {
      return userDao.getById(userId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }
}
