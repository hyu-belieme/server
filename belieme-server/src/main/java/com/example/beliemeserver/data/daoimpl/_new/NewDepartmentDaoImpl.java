package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.entity._new.NewMajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity._new.NewMajorEntity;
import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.DepartmentDao;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.MajorDto;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewDepartmentDaoImpl extends NewBaseDaoImpl implements DepartmentDao {

    public NewDepartmentDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<DepartmentDto> getAllList() {
        List<DepartmentDto> output = new ArrayList<>();

        for (NewDepartmentEntity departmentEntity : departmentRepository.findAll()) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public List<DepartmentDto> getListByUniversity(String universityName) {
        List<DepartmentDto> output = new ArrayList<>();

        UUID universityId = findUniversityEntity(universityName).getId();
        for (NewDepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public List<DepartmentDto> getListByUniversity(UUID universityId) {
        List<DepartmentDto> output = new ArrayList<>();

        for (NewDepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public DepartmentDto getById(UUID departmentId) {
        return findDepartmentEntity(departmentId).toDepartmentDto();
    }

    @Override
    public DepartmentDto getByIndex(String universityName, String departmentName) {
        NewDepartmentEntity targetEntity = findDepartmentEntity(universityName, departmentName);
        return targetEntity.toDepartmentDto();
    }

    @Override
    public boolean checkExistByIndex(String universityName, String departmentName) {
        try {
            UUID universityId = findUniversityEntity(universityName).getId();
            return departmentRepository.existsByUniversityIdAndName(universityId, departmentName);
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public DepartmentDto create(DepartmentDto newDepartment) {
        NewDepartmentEntity newDepartmentEntity = saveDepartmentOnly(newDepartment);
        newDepartmentEntity = saveBaseMajorJoins(newDepartmentEntity, newDepartment.baseMajors());

        return departmentRepository.save(newDepartmentEntity).toDepartmentDto();
    }

    @Override
    public DepartmentDto update(String universityName, String departmentName, DepartmentDto newDepartment) {
        NewDepartmentEntity target = findDepartmentEntity(universityName, departmentName);
        target = updateDepartmentOnly(target, newDepartment);

        target = removeAllBaseMajorJoins(target);
        target = saveBaseMajorJoins(target, newDepartment.baseMajors());

        return departmentRepository.save(target).toDepartmentDto();
    }

    @Override
    public DepartmentDto update(UUID departmentId, DepartmentDto newDepartment) {
        NewDepartmentEntity target = findDepartmentEntity(departmentId);
        target = updateDepartmentOnly(target, newDepartment);

        target = removeAllBaseMajorJoins(target);
        target = saveBaseMajorJoins(target, newDepartment.baseMajors());

        return departmentRepository.save(target).toDepartmentDto();
    }

    private NewDepartmentEntity saveDepartmentOnly(DepartmentDto newDepartment) {
        NewUniversityEntity university = findUniversityEntity(newDepartment.university().id());

        checkDepartmentConflict(university.getId(), newDepartment.name());

        NewDepartmentEntity newDepartmentEntity = new NewDepartmentEntity(
                newDepartment.id(),
                university,
                newDepartment.name()
        );

        return departmentRepository.save(newDepartmentEntity);
    }

    private NewDepartmentEntity updateDepartmentOnly(NewDepartmentEntity target, DepartmentDto newDepartment) {
        NewUniversityEntity newUniversityEntity = findUniversityEntity(newDepartment.university().id());

        if (doesIndexOfDepartmentChange(target, newDepartment)) {
            checkDepartmentConflict(newUniversityEntity.getId(), newDepartment.name());
        }

        return target
                .withName(newDepartment.name())
                .withUniversity(newUniversityEntity);
    }

    private NewDepartmentEntity saveBaseMajorJoins(NewDepartmentEntity newDepartmentEntity, List<MajorDto> baseMajors) {
        for (MajorDto baseMajor : baseMajors) {
            NewMajorEntity baseMajorEntity = findMajorEntity(baseMajor.id());
            NewMajorDepartmentJoinEntity newJoin = new NewMajorDepartmentJoinEntity(
                    baseMajorEntity,
                    newDepartmentEntity
            );
            NewMajorDepartmentJoinEntity newMajorJoin = majorDepartmentJoinRepository.save(newJoin);
            newDepartmentEntity = newDepartmentEntity.withBaseMajorAdd(newMajorJoin);
        }
        return newDepartmentEntity;
    }

    private NewDepartmentEntity removeAllBaseMajorJoins(NewDepartmentEntity department) {
        majorDepartmentJoinRepository.deleteAll(department.getBaseMajorJoin());
        return department.withBaseMajorClear();
    }

    private boolean doesIndexOfDepartmentChange(NewDepartmentEntity target, DepartmentDto newDepartment) {
        UUID oldUniversityId = target.getUniversity().getId();
        String oldDepartmentName = target.getName();
        return !(oldUniversityId.equals(newDepartment.university().id())
                && oldDepartmentName.equals(newDepartment.name()));
    }

    private void checkDepartmentConflict(UUID universityId, String departmentName) {
        if (departmentRepository.existsByUniversityIdAndName(universityId, departmentName)) {
            throw new ConflictException();
        }
    }
}
