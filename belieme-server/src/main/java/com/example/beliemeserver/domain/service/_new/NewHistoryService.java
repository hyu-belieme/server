package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.*;
import com.example.beliemeserver.domain.dto.enumeration.HistoryStatus;
import com.example.beliemeserver.domain.dto.enumeration.ItemStatus;
import com.example.beliemeserver.domain.exception.*;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewHistoryService extends NewBaseService {
    public NewHistoryService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<HistoryDto> getListByDepartment(
            @NonNull String userToken, @NonNull UUID departmentId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
        checkStaffPermission(requester, department);
        return historyDao.getListByDepartment(departmentId);
    }

    public List<HistoryDto> getListByStuff(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
        checkStaffPermission(requester, stuff.department());
        return historyDao.getListByStuff(stuffId);
    }

    public List<HistoryDto> getListByItem(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        ItemDto item = getItemOrThrowInvalidIndexException(itemId);
        checkStaffPermission(requester, item.stuff().department());
        return historyDao.getListByItem(itemId);
    }

    public List<HistoryDto> getListByDepartmentAndRequester(
            @NonNull String userToken, @NonNull UUID departmentId, @NonNull UUID userId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
        UserDto historyRequester = getUserOrThrowInvalidIndexException(userId);

        if (!requester.matchUniqueKey(historyRequester)) {
            checkStaffPermission(requester, department);
        }
        checkUserPermission(requester, department);

        return historyDao.getListByDepartmentAndRequester(departmentId, userId);
    }

    public HistoryDto getById(@NonNull String userToken, @NonNull UUID historyId) {
        UserDto requester = validateTokenAndGetUser(userToken);

        HistoryDto history = historyDao.getById(historyId);
        if (!requester.matchUniqueKey(history.requester())) {
            checkStaffPermission(requester, history.item().stuff().department());
        }
        checkUserPermission(requester, history.item().stuff().department());
        return history;
    }

    public HistoryDto createReservationOnStuff(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
        DepartmentDto department = stuff.department();
        checkUserPermission(requester, department);
        checkRequesterRentalList(stuff, requester);

        ItemDto item = stuff.firstUsableItem();
        if (item == null) throw new UsableItemNotExistedException();

        return createRentalHistory(requester, item);
    }

    public HistoryDto createReservationOnItem(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        ItemDto item = getItemOrThrowInvalidIndexException(itemId);
        DepartmentDto department = item.stuff().department();
        checkUserPermission(requester, department);
        checkRequesterRentalList(item.stuff(), requester);

        return createRentalHistory(requester, item);
    }

    public HistoryDto makeItemLost(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        ItemDto item = getItemOrThrowInvalidIndexException(itemId);
        DepartmentDto department = item.stuff().department();
        checkStaffPermission(requester, department);

        if (item.status() == ItemStatus.LOST) throw new LostRegistrationRequestedOnLostItemException();

        HistoryDto newHistory;
        if (item.isUsable() || item.status() == ItemStatus.REQUESTED) {
            if (item.isUnusable() && item.lastHistory().status() == HistoryStatus.REQUESTED) {
                makeItemCancel(userToken, itemId);
            }
            newHistory = HistoryDto.init(
                    item,
                    item.nextHistoryNum(),
                    null,
                    null,
                    null,
                    requester,
                    null,
                    0,
                    0,
                    0,
                    System.currentTimeMillis() / 1000,
                    0
            );
            UUID newHistoryId = historyDao.create(newHistory).id();
            itemDao.update(item.id(), item.withLastHistory(newHistory));

            return historyDao.getById(newHistoryId);
        }

        newHistory = item.lastHistory()
                .withItem(item)
                .withLostManager(requester)
                .withLostAt(System.currentTimeMillis() / 1000);
        return historyDao.update(newHistory.id(), newHistory);
    }

    public HistoryDto makeItemUsing(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        ItemDto item = getItemOrThrowInvalidIndexException(itemId);
        DepartmentDto department = item.stuff().department();
        checkStaffPermission(requester, department);

        HistoryDto lastHistory = item.lastHistory();
        if (lastHistory == null
                || lastHistory.status() != HistoryStatus.REQUESTED) {
            throw new RespondedOnUnrequestedItemException();
        }

        HistoryDto newHistory = lastHistory
                .withItem(item)
                .withApproveManager(requester)
                .withApprovedAt(System.currentTimeMillis() / 1000);
        return historyDao.update(lastHistory.id(), newHistory);
    }

    public HistoryDto makeItemReturn(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        ItemDto item = getItemOrThrowInvalidIndexException(itemId);
        DepartmentDto department = item.stuff().department();
        checkStaffPermission(requester, department);

        HistoryDto lastHistory = item.lastHistory();
        if (lastHistory == null
                || (lastHistory.status() != HistoryStatus.USING
                && lastHistory.status() != HistoryStatus.DELAYED
                && lastHistory.status() != HistoryStatus.LOST)) {
            throw new ReturnRegistrationRequestedOnReturnedItemException();
        }

        HistoryDto newHistory = lastHistory
                .withItem(item)
                .withReturnManager(requester)
                .withReturnedAt(System.currentTimeMillis() / 1000);
        return historyDao.update(newHistory.id(), newHistory);
    }

    public HistoryDto makeItemCancel(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);

        ItemDto item = getItemOrThrowInvalidIndexException(itemId);
        DepartmentDto department = item.stuff().department();
        checkStaffPermission(requester, department);

        HistoryDto lastHistory = item.lastHistory();
        if (lastHistory == null
                || lastHistory.status() != HistoryStatus.REQUESTED) {
            throw new RespondedOnUnrequestedItemException();
        }

        HistoryDto newHistory = lastHistory
                .withItem(item)
                .withCancelManager(requester)
                .withCanceledAt(System.currentTimeMillis() / 1000);

        return historyDao.update(newHistory.id(), newHistory);
    }

    private void checkRequesterRentalList(StuffDto stuff, UserDto requester) {
        List<HistoryDto> requesterHistory = historyDao.getListByDepartmentAndRequester(stuff.department().id(), requester.id());

        int usingItemCount = 0;
        int usingSameStuffCount = 0;
        for (HistoryDto history : requesterHistory) {
            if (history.status().isClosed()) continue;
            if (history.item().stuff().matchUniqueKey(stuff)) usingSameStuffCount += 1;
            usingItemCount += 1;

            if (usingItemCount >= Constants.MAX_RENTAL_COUNT) throw new RentalCountLimitExceededException();
            if (usingSameStuffCount >= Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF) {
                throw new RentalCountOnSameStuffLimitExceededException();
            }
        }
    }

    private HistoryDto createRentalHistory(UserDto requester, ItemDto item) {
        if (item.isUnusable()) throw new ReservationRequestedOnNonUsableItemException();
        HistoryDto newHistory = HistoryDto.init(
                item,
                item.nextHistoryNum(),
                requester,
                null,
                null,
                null,
                null,
                System.currentTimeMillis() / 1000,
                0,
                0,
                0,
                0
        );
        UUID newHistoryId = historyDao.create(newHistory).id();
        itemDao.update(item.id(), item.withLastHistory(newHistory));

        return historyDao.getById(newHistoryId);
    }

    private UserDto getUserOrThrowInvalidIndexException(UUID userId) {
        try {
            return userDao.getById(userId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }
}
