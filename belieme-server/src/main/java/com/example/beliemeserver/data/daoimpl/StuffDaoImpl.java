package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.model.dao.StuffDao;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StuffDaoImpl implements StuffDao {
    @Override
    public List<StuffDto> getListByDepartment(String universityCode, String departmentCode) throws DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public StuffDto getByIndex(String universityCode, String departmentCode, String stuffName) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public StuffDto create(StuffDto newStuff) throws ConflictException, NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public StuffDto update(String universityCode, String departmentCode, String stuffName, StuffDto newStuff) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }
}
