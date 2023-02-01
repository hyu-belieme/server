package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.MethodNotAllowedException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService extends BaseService {

    public static final int MAX_LENTAL_COUNT = 3;
    public static final int MAX_LENTAL_COUNT_ON_SAME_STUFF = 1;

    public HistoryService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<HistoryDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkStaffPermission(userToken, department);
        return historyDao.getListByDepartment(universityCode, departmentCode);
    }

    public List<HistoryDto> getListByStuff(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkStaffPermission(userToken, department);
        return getHistoryListByStuffOrThrowInvalidIndexException(universityCode, departmentCode, stuffName);
    }

    public List<HistoryDto> getListByItem(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkStaffPermission(userToken, department);
        return getHistoryListByItemOrThrowInvalidIndexException(universityCode, departmentCode, stuffName, itemNum);
    }

    public List<HistoryDto> getListByDepartmentAndRequester(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String userUniversityCode, @NonNull String userStudentId
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        UserDto requester = checkTokenAndGetUser(userToken);
        UserDto historyRequester = getUserOrThrowInvalidIndexException(userUniversityCode, userStudentId);

        if(!requester.matchUniqueKey(historyRequester)) {
            checkStaffPermission(department, requester);
        }
        checkUserPermission(department, requester);

        return historyDao.getListByDepartmentAndRequester(universityCode, departmentCode, userUniversityCode, userStudentId);
    }

    public HistoryDto getByIndex(@NonNull String userToken,
                                 @NonNull String universityCode, @NonNull String departmentCode,
                                 @NonNull String stuffName, int itemNum, int historyNum) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        UserDto requester = checkTokenAndGetUser(userToken);

        HistoryDto target = historyDao.getByIndex(universityCode, departmentCode, stuffName, itemNum, historyNum);
        if(!requester.matchUniqueKey(target.requester())) {
            checkStaffPermission(department, requester);
        }
        checkUserPermission(department, requester);

        return target;
    }

    public HistoryDto createReservation(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, Integer itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        UserDto requester = checkTokenAndGetUser(userToken);
        checkUserPermission(department, requester);

        StuffDto stuff = getStuffOrThrowInvalidIndexException(universityCode, departmentCode, stuffName);
        List<HistoryDto> requesterHistory = historyDao.getListByDepartmentAndRequester(universityCode, departmentCode, requester.university().code(), requester.studentId());

        int usingItemCount = 0;
        int usingSameStuffCount = 0;
        for(HistoryDto history : requesterHistory) {
            if(history.status().isClosed()) continue;
            if(history.item().stuff().matchUniqueKey(stuff)) usingSameStuffCount += 1;
            usingItemCount += 1;
            if(usingItemCount >= MAX_LENTAL_COUNT) throw new MethodNotAllowedException();
            if(usingSameStuffCount >= MAX_LENTAL_COUNT_ON_SAME_STUFF) throw new MethodNotAllowedException();
        }

        if(itemNum == null) {
            itemNum = stuff.firstUsableItemNum();
        }
        if(itemNum == 0) throw new MethodNotAllowedException();

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityCode, departmentCode, stuffName, itemNum);

        if(item.status() != ItemDto.ItemStatus.USABLE) throw new MethodNotAllowedException();

        HistoryDto newHistory = new HistoryDto(
                item,
                item.nextHistoryNum(),
                requester,
                null,
                null,
                null,
                null,
                System.currentTimeMillis()/1000,
                0,
                0,
                0,
                0
        );
        historyDao.create(newHistory);
        itemDao.update(universityCode, departmentCode,
                stuffName, itemNum, item.withLastHistory(newHistory));

        return historyDao.getByIndex(universityCode, departmentCode,
                stuffName, itemNum, newHistory.num());
    }

    public HistoryDto makeItemLost(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        UserDto requester = checkTokenAndGetUser(userToken);
        checkStaffPermission(department, requester);

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityCode, departmentCode, stuffName, itemNum);

        if(item.status() == ItemDto.ItemStatus.INACTIVE) throw new MethodNotAllowedException();

        HistoryDto newHistory;
        if(item.status() == ItemDto.ItemStatus.USABLE) {
            newHistory = new HistoryDto(
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
                    System.currentTimeMillis()/1000,
                    0
            );
            historyDao.create(newHistory);
            itemDao.update(universityCode, departmentCode,
                    stuffName, itemNum, item.withLastHistory(newHistory));
            return historyDao.getByIndex(universityCode, departmentCode,
                    stuffName, itemNum, newHistory.num());
        }

        newHistory = historyDao.getByIndex(universityCode, departmentCode, stuffName, itemNum, item.lastHistory().num());
        newHistory = newHistory
                .withLostManager(requester)
                .withLostTimeStamp(System.currentTimeMillis()/1000);

        return historyDao.update(universityCode, departmentCode, stuffName, itemNum, newHistory.num(), newHistory);
    }

    public HistoryDto makeItemUsing(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        UserDto requester = checkTokenAndGetUser(userToken);
        checkStaffPermission(department, requester);

        ItemDto item = getItemOrThrowInvalidIndexException(
                universityCode, departmentCode, stuffName, itemNum);

        HistoryDto lastHistory = item.lastHistory();
        if(lastHistory == null
                || lastHistory.status() != HistoryDto.HistoryStatus.REQUESTED) {
            throw new MethodNotAllowedException();
        }

        HistoryDto newHistory = lastHistory
                .withApproveManager(requester)
                .withApprovalTimeStamp(System.currentTimeMillis()/1000);

        return historyDao.update(universityCode, departmentCode, stuffName, itemNum, newHistory.num(), newHistory);
    }

    public HistoryDto makeItemReturned(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        // TODO Need to implements.
        return null;
    }

    public HistoryDto makeItemCancel(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        // TODO Need to implements.
        return null;
    }

    private List<HistoryDto> getHistoryListByStuffOrThrowInvalidIndexException(
            String universityCode, String departmentCode, String stuffName
    ) {
        try {
            return historyDao.getListByStuff(universityCode, departmentCode, stuffName);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    private List<HistoryDto> getHistoryListByItemOrThrowInvalidIndexException(
            String universityCode, String departmentCode, String stuffName, int itemNum
    ) {
        try {
            return historyDao.getListByItem(universityCode, departmentCode, stuffName, itemNum);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    private UserDto getUserOrThrowInvalidIndexException(String universityCode, String studentId) {
        try {
            return userDao.getByIndex(universityCode, studentId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }
}
