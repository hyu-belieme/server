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

@Service
public class HistoryService extends BaseService {
    public HistoryService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<HistoryDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        checkStaffPermission(userToken, department);
        return historyDao.getListByDepartment(universityName, departmentName);
    }

    public List<HistoryDto> getListByStuff(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        checkStaffPermission(userToken, department);
        return getHistoryListByStuffOrThrowInvalidIndexException(universityName, departmentName, stuffName);
    }

    public List<HistoryDto> getListByItem(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        checkStaffPermission(userToken, department);
        return getHistoryListByItemOrThrowInvalidIndexException(universityName, departmentName, stuffName, itemNum);
    }

    public List<HistoryDto> getListByDepartmentAndRequester(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String userUniversityName, @NonNull String userStudentId
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);
        UserDto historyRequester = getUserOrThrowInvalidIndexException(userUniversityName, userStudentId);

        if (!requester.matchUniqueKey(historyRequester)) {
            checkStaffPermission(department, requester);
        }
        checkUserPermission(department, requester);

        return historyDao.getListByDepartmentAndRequester(universityName, departmentName, userUniversityName, userStudentId);
    }

    public HistoryDto getByIndex(@NonNull String userToken,
                                 @NonNull String universityName, @NonNull String departmentName,
                                 @NonNull String stuffName, int itemNum, int historyNum) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);

        HistoryDto target = historyDao.getByIndex(universityName, departmentName, stuffName, itemNum, historyNum);
        if (!requester.matchUniqueKey(target.requester())) {
            checkStaffPermission(department, requester);
        }
        checkUserPermission(department, requester);

        return target;
    }

    public HistoryDto createReservation(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, Integer itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);
        checkUserPermission(department, requester);

        StuffDto stuff = getStuffOrThrowInvalidIndexException(universityName, departmentName, stuffName);
        List<HistoryDto> requesterHistory = historyDao.getListByDepartmentAndRequester(universityName, departmentName, requester.university().name(), requester.studentId());

        int usingItemCount = 0;
        int usingSameStuffCount = 0;
        for (HistoryDto history : requesterHistory) {
            if (history.status().isClosed()) continue;
            if (history.item().stuff().matchUniqueKey(stuff)) usingSameStuffCount += 1;
            usingItemCount += 1;
            if (usingItemCount >= Constants.MAX_RENTAL_COUNT) throw new RentalCountLimitExceededException();
            if (usingSameStuffCount >= Constants.MAX_RENTAL_COUNT_ON_SAME_STUFF)
                throw new RentalCountOnSameStuffLimitExceededException();
        }

        if (itemNum == null) {
            itemNum = stuff.firstUsableItemNum();
        }
        if (itemNum == 0) throw new UsableItemNotExistedException();

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityName, departmentName, stuffName, itemNum);

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
        historyDao.create(newHistory);
        itemDao.update(universityName, departmentName,
                stuffName, itemNum, item.withLastHistory(newHistory));

        return historyDao.getByIndex(universityName, departmentName,
                stuffName, itemNum, newHistory.num());
    }

    public HistoryDto makeItemLost(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);
        checkStaffPermission(department, requester);

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityName, departmentName, stuffName, itemNum);

        if (item.status() == ItemStatus.LOST) throw new LostRegistrationRequestedOnLostItemException();

        HistoryDto newHistory;
        if (item.isUsable() || item.status() == ItemStatus.REQUESTED) {
            if (item.isUnusable() && item.lastHistory().status() == HistoryStatus.REQUESTED) {
                makeItemCancel(userToken, universityName, departmentName, stuffName, itemNum);
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
            historyDao.create(newHistory);
            itemDao.update(universityName, departmentName,
                    stuffName, itemNum, item.withLastHistory(newHistory));
            return historyDao.getByIndex(universityName, departmentName,
                    stuffName, itemNum, newHistory.num());
        }

        newHistory = item.lastHistory()
                .withItem(item)
                .withLostManager(requester)
                .withLostAt(System.currentTimeMillis() / 1000);
        return historyDao.update(universityName, departmentName, stuffName, itemNum, newHistory.num(), newHistory);
    }

    public HistoryDto makeItemUsing(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);
        checkStaffPermission(department, requester);

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityName, departmentName, stuffName, itemNum);

        HistoryDto lastHistory = item.lastHistory();
        if (lastHistory == null
                || lastHistory.status() != HistoryStatus.REQUESTED) {
            throw new RespondedOnUnrequestedItemException();
        }

        HistoryDto newHistory = lastHistory
                .withItem(item)
                .withApproveManager(requester)
                .withApprovedAt(System.currentTimeMillis() / 1000);

        return historyDao.update(universityName, departmentName, stuffName, itemNum, newHistory.num(), newHistory);
    }

    public HistoryDto makeItemReturn(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);
        checkStaffPermission(department, requester);

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityName, departmentName, stuffName, itemNum);

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

        return historyDao.update(universityName, departmentName, stuffName, itemNum, newHistory.num(), newHistory);
    }

    public HistoryDto makeItemCancel(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        UserDto requester = validateTokenAndGetUser(userToken);
        checkStaffPermission(department, requester);

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityName, departmentName, stuffName, itemNum);

        HistoryDto lastHistory = item.lastHistory();
        if (lastHistory == null
                || lastHistory.status() != HistoryStatus.REQUESTED) {
            throw new RespondedOnUnrequestedItemException();
        }

        HistoryDto newHistory = lastHistory
                .withItem(item)
                .withCancelManager(requester)
                .withCanceledAt(System.currentTimeMillis() / 1000);

        return historyDao.update(universityName, departmentName, stuffName, itemNum, newHistory.num(), newHistory);
    }

    private List<HistoryDto> getHistoryListByStuffOrThrowInvalidIndexException(
            String universityName, String departmentName, String stuffName
    ) {
        try {
            return historyDao.getListByStuff(universityName, departmentName, stuffName);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    private List<HistoryDto> getHistoryListByItemOrThrowInvalidIndexException(
            String universityName, String departmentName, String stuffName, int itemNum
    ) {
        try {
            return historyDao.getListByItem(universityName, departmentName, stuffName, itemNum);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    private UserDto getUserOrThrowInvalidIndexException(String universityName, String studentId) {
        try {
            return userDao.getByIndex(universityName, studentId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }
}
