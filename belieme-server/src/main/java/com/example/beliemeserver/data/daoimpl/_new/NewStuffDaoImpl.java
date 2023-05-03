package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.StuffDao;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.error.exception.ConflictException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewStuffDaoImpl extends NewBaseDaoImpl implements StuffDao {
    public NewStuffDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<StuffDto> getAllList() {
        List<StuffDto> output = new ArrayList<>();
        for (NewStuffEntity stuffEntity : stuffRepository.findAll()) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public List<StuffDto> getListByDepartment(UUID departmentId) {
        List<StuffDto> output = new ArrayList<>();
        for (NewStuffEntity stuffEntity : stuffRepository.findByDepartmentId(departmentId)) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public List<StuffDto> getListByDepartment(String universityName, String departmentName) {
        NewDepartmentEntity departmentOfTarget = findDepartmentEntity(universityName, departmentName);

        List<StuffDto> output = new ArrayList<>();
        for (NewStuffEntity stuffEntity : stuffRepository.findByDepartmentId(departmentOfTarget.getId())) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public StuffDto getById(UUID stuffId) {
        return findStuffEntity(stuffId).toStuffDto();
    }

    @Override
    public StuffDto getByIndex(String universityName, String departmentName, String stuffName) {
        return findStuffEntity(universityName, departmentName, stuffName).toStuffDto();
    }

    @Override
    public StuffDto create(StuffDto newStuff) {
        NewDepartmentEntity departmentOfNewStuff = findDepartmentEntity(newStuff.department().id());

        checkStuffConflict(departmentOfNewStuff.getId(), newStuff.name());

        NewStuffEntity newStuffEntity = new NewStuffEntity(
                newStuff.id(),
                departmentOfNewStuff,
                newStuff.name(),
                newStuff.thumbnail()
        );
        return stuffRepository.save(newStuffEntity).toStuffDto();
    }

    @Override
    public StuffDto update(UUID stuffId, StuffDto newStuff) {
        NewStuffEntity target = findStuffEntity(stuffId);
        NewDepartmentEntity departmentOfNewStuff = findDepartmentEntity(newStuff.department().id());

        if (doesIndexChange(target, newStuff)) {
            checkStuffConflict(departmentOfNewStuff.getId(), newStuff.name());
        }

        target = target.withDepartment(departmentOfNewStuff)
                .withName(newStuff.name())
                .withThumbnail(newStuff.thumbnail());
        return stuffRepository.save(target).toStuffDto();
    }

    @Override
    public StuffDto update(String universityName, String departmentName, String stuffName, StuffDto newStuff) {
        NewStuffEntity target = findStuffEntity(universityName, departmentName, stuffName);
        NewDepartmentEntity departmentOfNewStuff = findDepartmentEntity(newStuff.department().id());

        if (doesIndexChange(target, newStuff)) {
            checkStuffConflict(departmentOfNewStuff.getId(), newStuff.name());
        }

        target = target.withDepartment(departmentOfNewStuff)
                .withName(newStuff.name())
                .withThumbnail(newStuff.thumbnail());
        return stuffRepository.save(target).toStuffDto();
    }

    private boolean doesIndexChange(NewStuffEntity target, StuffDto newStuff) {
        String oldUniversityName = target.getDepartment().getUniversity().getName();
        String oldDepartmentName = target.getDepartment().getName();
        String oldName = target.getName();

        return !(oldUniversityName.equals(newStuff.department().university().name())
                && oldDepartmentName.equals(newStuff.department().name())
                && oldName.equals(newStuff.name()));
    }

    private void checkStuffConflict(UUID departmentId, String name) {
        if (stuffRepository.existsByDepartmentIdAndName(departmentId, name)) {
            throw new ConflictException();
        }
    }
}
