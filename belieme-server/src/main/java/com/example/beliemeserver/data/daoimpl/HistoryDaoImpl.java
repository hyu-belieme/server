package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.model.dao.HistoryDao;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public class HistoryDaoImpl implements HistoryDao {
    @Override
    public List<HistoryDto> getListByDepartment(String universityCode, String departmentCode) throws DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public List<HistoryDto> getListByDepartmentAndRequester(String universityCodeForDepartment, String departmentCode, String universityCodeForUser, String requesterStudentId) throws DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public HistoryDto getByIndex(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public HistoryDto create(HistoryDto newHistory) throws ConflictException, NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public HistoryDto update(String universityCode, String departmentCode, String stuffName, int itemNum, int historyNum, HistoryDto newHistory) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }
}
