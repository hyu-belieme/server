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
public class DepartmentDaoImpl extends NewBaseDaoImpl implements DepartmentDao {

    public DepartmentDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
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
        saveBaseMajorJoins(newDepartmentEntity, newDepartment.baseMajors());

        return newDepartmentEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto update(String universityName, String departmentName, DepartmentDto newDepartment) {
        NewDepartmentEntity target = findDepartmentEntity(universityName, departmentName);
        updateDepartmentOnly(target, newDepartment);

        removeAllBaseMajorJoins(target);
        saveBaseMajorJoins(target, newDepartment.baseMajors());

        return target.toDepartmentDto();
    }

    private NewDepartmentEntity saveDepartmentOnly(DepartmentDto newDepartment) {
        NewUniversityEntity university = findUniversityEntity(newDepartment.university());

        checkDepartmentConflict(university.getId(), newDepartment.name());

        NewDepartmentEntity newDepartmentEntity = new NewDepartmentEntity(
                newDepartment.id(),
                university,
                newDepartment.name()
        );

        return departmentRepository.save(newDepartmentEntity);
    }

    private void updateDepartmentOnly(NewDepartmentEntity target, DepartmentDto newDepartment) {
        String newUniversityName = newDepartment.university().name();
        NewUniversityEntity newUniversityEntity = findUniversityEntity(newUniversityName);

        if (doesIndexOfDepartmentChange(target, newDepartment)) {
            checkDepartmentConflict(newUniversityEntity.getId(), newDepartment.name());
        }

        target.setName(newDepartment.name())
                .setUniversity(newUniversityEntity);
    }

    private void saveBaseMajorJoins(NewDepartmentEntity newDepartmentEntity, List<MajorDto> baseMajors) {
        for (MajorDto baseMajor : baseMajors) {
            NewMajorEntity baseMajorEntity = findMajorEntity(baseMajor);
            NewMajorDepartmentJoinEntity newJoin = new NewMajorDepartmentJoinEntity(
                    baseMajorEntity,
                    newDepartmentEntity
            );
            majorDepartmentJoinRepository.save(newJoin);
        }
    }

    private void removeAllBaseMajorJoins(NewDepartmentEntity department) {
        while (department.getBaseMajorJoin().size() > 0) {
            majorDepartmentJoinRepository.delete(department.getBaseMajorJoin().get(0));
        }
    }

    private boolean doesIndexOfDepartmentChange(NewDepartmentEntity target, DepartmentDto newDepartment) {
        String oldUniversityName = target.getUniversity().getName();
        String oldDepartmentName = target.getName();
        return !(oldUniversityName.equals(newDepartment.university().name())
                && oldDepartmentName.equals(newDepartment.name()));
    }

    private void checkDepartmentConflict(UUID universityId, String departmentName) {
        if (departmentRepository.existsByUniversityIdAndName(universityId, departmentName)) {
            throw new ConflictException();
        }
    }
}
