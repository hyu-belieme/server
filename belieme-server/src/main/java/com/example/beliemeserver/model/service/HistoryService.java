package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.*;
import com.example.beliemeserver.model.util.AuthCheck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final ItemDao itemDao;
    private final HistoryDao historyDao;
    private final AuthCheck authCheck;

    public HistoryService(ItemDao itemDao, HistoryDao historyDao, UserDao userDao) {
        this.itemDao = itemDao;
        this.historyDao = historyDao;

        this.authCheck = new AuthCheck(userDao);
    }

    public List<HistoryDto> getHistories(String userToken, String studentId) throws DataException, UnauthorizedException, ForbiddenException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);

        checkIfRequesterHasPermissionToStudentId(requester, studentId);

        if(studentId == null) {
            return historyDao.getHistoriesData();
        } else {
            return historyDao.getHistoriesByRequesterIdData(studentId);
        }
    }

    public HistoryDto getHistoryByStuffNameAndItemNumAndHistoryNum(String userToken, String stuffName, int itemNum, int historyNum) throws DataException, UnauthorizedException, NotFoundException, ForbiddenException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        HistoryDto target = historyDao.getHistoryByStuffNameAndItemNumAndHistoryNumData(stuffName, itemNum, historyNum);
        checkIfRequesterHasPermissionToStudentId(requester, target.getRequesterId());

        return target;
    }

    public HistoryDto addReserveHistory(String userToken, String stuffName, Integer itemNum) throws DataException, UnauthorizedException, ForbiddenException, BadRequestException, MethodNotAllowedException, ConflictException, ItCannotBeException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasUserPermission(requester);

        if(stuffName == null) {
            throw new BadRequestException("정보가 부족합니다.\n필요한 정보 : stuffName(String), itemNum(Int)(Optional)");
        }

        ItemDto target;
        if(itemNum == null) {
            target = getUsableItem(stuffName);
        } else {
            try {
                target = itemDao.getItemByStuffNameAndItemNumData(stuffName, itemNum);
            } catch (NotFoundException e) {
                e.printStackTrace();
                throw new MethodNotAllowedException("해당 물품은 사용할 수 없습니다.");
            }
        }

        HistoryDto newHistoryDto = HistoryDto.builder()
                .item(target)
                .requester(requester)
                .reservedTimeStamp(System.currentTimeMillis()/1000)
                .build();

        try {
            return historyDao.addHistoryData(newHistoryDto);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new ItCannotBeException();
        }
    }

    private ItemDto getUsableItem(String stuffName) throws DataException, MethodNotAllowedException {
        List<ItemDto> itemsByStuffName = itemDao.getItemsByStuffNameData(stuffName);

        ItemDto target = null;
        for(int i = 0; i < itemsByStuffName.size(); i++) {
            target = itemsByStuffName.get(i);
            if(target.getState() == ItemDto.ItemStatus.USABLE) {
                break;
            }
            target = null;
        }
        if(target == null) {
            throw new MethodNotAllowedException("사용할 수 있는 물품이 없습니다.");
        }
        return target;
    }

    public HistoryDto addOrEditToLostHistory(String userToken, String stuffName, int itemNum) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, ItCannotBeException, MethodNotAllowedException, ConflictException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
//        authCheck.checkIfRequesterHasStaffPermission(requester);
        authCheck.checkIfRequesterHasUserPermission(requester);

        ItemDto targetItem = itemDao.getItemByStuffNameAndItemNumData(stuffName, itemNum);

        boolean shouldPostNewHistory;
        HistoryDto targetHistory;
        if(targetItem.getState() == ItemDto.ItemStatus.UNUSABLE) {
            try {
                targetHistory = historyDao.getHistoryByStuffNameAndItemNumAndHistoryNumData(stuffName, itemNum, targetItem.getLastHistoryNum());
            } catch (NotFoundException e) {
                throw new ItCannotBeException();
            }
            shouldPostNewHistory = false;
        } else if(targetItem.getState() == ItemDto.ItemStatus.USABLE) {
            targetHistory = HistoryDto.builder()
                    .item(targetItem)
                    .build();
            shouldPostNewHistory = true;
        } else {
            throw new MethodNotAllowedException("이미 분실 신고된 물품입니다.");
        }

        targetHistory.setLostManager(requester);
        targetHistory.setLostTimeStamp(System.currentTimeMillis()/1000);

        if(shouldPostNewHistory) {
            try {
                return historyDao.addHistoryData(targetHistory);
            } catch (NotFoundException e) {
                e.printStackTrace();
                throw new ItCannotBeException();
            }
        } else {
            try {
                return historyDao.updateHistoryData(stuffName, itemNum, targetHistory.getNum(), targetHistory);
            } catch (NotFoundException e) {
                e.printStackTrace();
                throw new ItCannotBeException();
            }
        }
    }

    public HistoryDto editToCanceledHistory(String userToken, String stuffName, int itemNum, int historyNum) throws DataException, UnauthorizedException, NotFoundException, ForbiddenException, MethodNotAllowedException, ItCannotBeException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);

        HistoryDto target = historyDao.getHistoryByStuffNameAndItemNumAndHistoryNumData(stuffName, itemNum, historyNum);

        checkIfRequesterHasPermissionToStudentId(requester, target.getRequesterId());
        checkIfStatusOfHistoryIsReserved(target);

        target.setCancelTimeStamp(System.currentTimeMillis()/1000);
        if(!requester.getStudentId().equals(target.getRequesterId())) {
            target.setCancelManager(requester);
        }

        try {
            return historyDao.updateHistoryData(stuffName, itemNum, historyNum, target);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new ItCannotBeException();
        }
    }

    public HistoryDto editToApprovedHistory(String userToken, String stuffName, int itemNum, int historyNum) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, MethodNotAllowedException, ItCannotBeException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
//        authCheck.checkIfRequesterHasStaffPermission(requester);
        authCheck.checkIfRequesterHasUserPermission(requester);

        HistoryDto target = historyDao.getHistoryByStuffNameAndItemNumAndHistoryNumData(stuffName, itemNum, historyNum);

        checkIfStatusOfHistoryIsReserved(target);

        target.setApproveTimeStamp(System.currentTimeMillis()/1000);
        target.setApproveManager(requester);

        try {
            return historyDao.updateHistoryData(stuffName, itemNum, historyNum, target);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new ItCannotBeException();
        }
    }

    public HistoryDto editToReturnedHistory(String userToken, String stuffName, int itemNum, int historyNum) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, MethodNotAllowedException, ItCannotBeException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
//        authCheck.checkIfRequesterHasStaffPermission(requester);
        authCheck.checkIfRequesterHasUserPermission(requester);

        HistoryDto target = historyDao.getHistoryByStuffNameAndItemNumAndHistoryNumData(stuffName, itemNum, historyNum);

        checkIfStatusOfHistoryIsUsingOrDelayed(target);

        target.setReturnTimeStamp(System.currentTimeMillis()/1000);
        target.setReturnManager(requester);

        try {
            return historyDao.updateHistoryData(stuffName, itemNum, historyNum, target);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new ItCannotBeException();
        }
    }

    public HistoryDto editToFoundHistory(String userToken, String stuffName, int itemNum, int historyNum) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, MethodNotAllowedException, ItCannotBeException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
//        authCheck.checkIfRequesterHasStaffPermission(requester);
        authCheck.checkIfRequesterHasUserPermission(requester);

        HistoryDto target = historyDao.getHistoryByStuffNameAndItemNumAndHistoryNumData(stuffName, itemNum, historyNum);

        checkIfStatusOfHistoryIsLost(target);

        target.setReturnTimeStamp(System.currentTimeMillis()/1000);
        target.setReturnManager(requester);
        try {
            return historyDao.updateHistoryData(stuffName, itemNum, historyNum, target);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new ItCannotBeException();
        }
    }

    private void checkIfRequesterHasPermissionToStudentId(UserDto requester, String studentId) throws ForbiddenException {
        authCheck.checkIfRequesterHasUserPermission(requester);
        if(!requester.getStudentId().equals(studentId)) {
//            authCheck.checkIfRequesterHasStaffPermission(requester);
            authCheck.checkIfRequesterHasUserPermission(requester);
        }
    }

    private void checkIfStatusOfHistoryIsReserved(HistoryDto history) throws MethodNotAllowedException {
        if(history.getStatus() != HistoryDto.HistoryStatus.REQUESTED) {
            throw new MethodNotAllowedException("'예약됨'기록이 아닙니다.");
        }
    }

    private void checkIfStatusOfHistoryIsUsingOrDelayed(HistoryDto history) throws MethodNotAllowedException {
        if(history.getStatus() != HistoryDto.HistoryStatus.USING && history.getStatus() != HistoryDto.HistoryStatus.DELAYED) {
            throw new MethodNotAllowedException("'사용중' 또는 '연체됨'기록이 아닙니다.");
        }
    }

    private void checkIfStatusOfHistoryIsLost(HistoryDto history) throws MethodNotAllowedException {
        if(history.getStatus() != HistoryDto.HistoryStatus.LOST) {
            throw new MethodNotAllowedException("'분실됨'기록이 아닙니다.");
        }
    }
}
