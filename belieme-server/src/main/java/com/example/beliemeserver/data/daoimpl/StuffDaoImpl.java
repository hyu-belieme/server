package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.StuffDao;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StuffDaoImpl extends BaseDaoImpl implements StuffDao {
    public StuffDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<StuffDto> getAllList() throws DataException {
        List<StuffDto> output = new ArrayList<>();
        for(StuffEntity stuffEntity : stuffRepository.findAll()) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public List<StuffDto> getListByDepartment(String universityCode, String departmentCode) throws DataException, NotFoundException {
        DepartmentEntity departmentOfTarget = getDepartmentEntity(universityCode, departmentCode);

        List<StuffDto> output = new ArrayList<>();
        for(StuffEntity stuffEntity : stuffRepository.findByDepartmentId(departmentOfTarget.getId())) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public StuffDto getByIndex(String universityCode, String departmentCode, String stuffName) throws NotFoundException, DataException {
        return getStuffEntity(universityCode, departmentCode, stuffName).toStuffDto();
    }

    @Override
    public StuffDto create(StuffDto newStuff) throws ConflictException, NotFoundException, DataException {
        DepartmentEntity departmentOfNewStuff = getDepartmentEntity(newStuff.department());

        checkStuffConflict(departmentOfNewStuff.getId(), newStuff.name());

        StuffEntity newStuffEntity = new StuffEntity(
                departmentOfNewStuff,
                newStuff.name(),
                newStuff.emoji()
        );
        return stuffRepository.save(newStuffEntity).toStuffDto();
    }

    @Override
    public StuffDto update(String universityCode, String departmentCode, String stuffName, StuffDto newStuff) throws NotFoundException, DataException, ConflictException {
        StuffEntity target = getStuffEntity(universityCode, departmentCode, stuffName);
        DepartmentEntity departmentOfNewStuff = getDepartmentEntity(newStuff.department());

        if(doesIndexChange(target, newStuff)) {
            checkStuffConflict(departmentOfNewStuff.getId(), newStuff.name());
        }

        target.setDepartment(departmentOfNewStuff)
                .setName(newStuff.name())
                .setEmoji(newStuff.emoji());
        return target.toStuffDto();
    }

    private boolean doesIndexChange(StuffEntity target, StuffDto newStuff) {
        String oldUniversityCode = target.getDepartment().getUniversity().getCode();
        String oldDepartmentCode = target.getDepartment().getCode();
        String oldName = target.getName();

        return !(oldUniversityCode.equals(newStuff.department().university().code())
                && oldDepartmentCode.equals(newStuff.department().code())
                && oldName.equals(newStuff.name()));
    }

    private void checkStuffConflict(int departmentId, String name) throws ConflictException {
        if(stuffRepository.existsByDepartmentIdAndName(departmentId, name)) {
            throw new ConflictException();
        }
    }
}
