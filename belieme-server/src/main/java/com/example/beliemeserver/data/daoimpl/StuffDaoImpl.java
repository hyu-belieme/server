package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.model.dao.StuffDao;
import com.example.beliemeserver.model.dto.StuffDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StuffDaoImpl extends BaseDaoImpl implements StuffDao {
    public StuffDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<StuffDto> getAllList() {
        List<StuffDto> output = new ArrayList<>();
        for (StuffEntity stuffEntity : stuffRepository.findAll()) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public List<StuffDto> getListByDepartment(String universityCode, String departmentCode) {
        DepartmentEntity departmentOfTarget = findDepartmentEntity(universityCode, departmentCode);

        List<StuffDto> output = new ArrayList<>();
        for (StuffEntity stuffEntity : stuffRepository.findByDepartmentId(departmentOfTarget.getId())) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public StuffDto getByIndex(String universityCode, String departmentCode, String stuffName) {
        return findStuffEntity(universityCode, departmentCode, stuffName).toStuffDto();
    }

    @Override
    public StuffDto create(StuffDto newStuff) {
        DepartmentEntity departmentOfNewStuff = findDepartmentEntity(newStuff.department());

        checkStuffConflict(departmentOfNewStuff.getId(), newStuff.name());

        StuffEntity newStuffEntity = new StuffEntity(
                departmentOfNewStuff,
                newStuff.name(),
                newStuff.emoji()
        );
        return stuffRepository.save(newStuffEntity).toStuffDto();
    }

    @Override
    public StuffDto update(String universityCode, String departmentCode, String stuffName, StuffDto newStuff) {
        StuffEntity target = findStuffEntity(universityCode, departmentCode, stuffName);
        DepartmentEntity departmentOfNewStuff = findDepartmentEntity(newStuff.department());

        if (doesIndexChange(target, newStuff)) {
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

    private void checkStuffConflict(int departmentId, String name) {
        if (stuffRepository.existsByDepartmentIdAndName(departmentId, name)) {
            throw new ConflictException();
        }
    }
}
