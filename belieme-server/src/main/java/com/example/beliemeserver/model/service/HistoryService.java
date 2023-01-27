package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService extends BaseService {
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

    public HistoryDto createReservation(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, Integer itemNum
    ) {
        // TODO Need to implements.
        return null;
    }

    public HistoryDto createLost(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        // TODO Need to implements.
        return null;
    }

    public HistoryDto patchToApproved(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum, int historyNum,
            @NonNull String managerUniversityCode,
            @NonNull String managerStudentId
    ) {
        // TODO Need to implements.
        return null;
    }

    public HistoryDto patchToReturned(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum, int historyNum,
            @NonNull String managerUniversityCode,
            @NonNull String managerStudentId
    ) {
        // TODO Need to implements.
        return null;
    }

    public HistoryDto patchToLost(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum, int historyNum,
            @NonNull String managerUniversityCode,
            @NonNull String managerStudentId
    ) {
        // TODO Need to implements.
        return null;
    }

    public HistoryDto patchToCancel(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum, int historyNum,
            @NonNull String managerUniversityCode,
            @NonNull String managerStudentId
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
