package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.HistoryDto;
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

    public List<HistoryDto> getListByDepartmentAndRequester(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String userUniversityCode, @NonNull String userStudentId
    ) {
        // TODO Need to implements.
        return new ArrayList<>();
    }

    public List<HistoryDto> getListByStuff(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName
    ) {
        // TODO Need to implements.
        return new ArrayList<>();
    }

    public List<HistoryDto> getListByItem(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        // TODO Need to implements.
        return new ArrayList<>();
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
}
