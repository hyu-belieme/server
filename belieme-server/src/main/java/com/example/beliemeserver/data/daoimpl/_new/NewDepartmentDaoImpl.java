package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.entity._new.NewMajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity._new.NewMajorEntity;
import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.DepartmentDao;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
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
    public List<DepartmentDto> getListByUniversity(@NonNull UUID universityId) {
        List<DepartmentDto> output = new ArrayList<>();

        for (NewDepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public DepartmentDto getById(@NonNull UUID departmentId) {
        return findDepartmentEntity(departmentId).toDepartmentDto();
    }

    @Override
    public boolean checkExistById(@NonNull UUID departmentId) {
        return departmentRepository.existsById(departmentId);
    }

    @Override
    public DepartmentDto create(@NonNull UUID departmentId, @NonNull UUID universityId, @NonNull String name, @NonNull List<UUID> majorId) {
        NewDepartmentEntity newDepartmentEntity = saveDepartmentOnly(departmentId, universityId, name);
        newDepartmentEntity = saveBaseMajorJoins(newDepartmentEntity, majorId);

        return departmentRepository.save(newDepartmentEntity).toDepartmentDto();
    }

    @Override
    public DepartmentDto update(@NonNull UUID departmentId, @NonNull UUID universityId, @NonNull String name, @NonNull List<UUID> majorId) {
        NewDepartmentEntity target = findDepartmentEntity(departmentId);
        target = updateDepartmentOnly(target, universityId, name);

        target = removeAllBaseMajorJoins(target);
        target = saveBaseMajorJoins(target, majorId);

        return departmentRepository.save(target).toDepartmentDto();
    }

    private NewDepartmentEntity saveDepartmentOnly(UUID departmentId, UUID universityId, String name) {
        NewUniversityEntity university = getUniversityEntityOrThrowInvalidIndexException(universityId);

        checkDepartmentIdConflict(departmentId);
        checkDepartmentConflict(university.getId(), name);

        NewDepartmentEntity newDepartmentEntity = new NewDepartmentEntity(departmentId, university, name);
        return departmentRepository.save(newDepartmentEntity);
    }

    private NewDepartmentEntity updateDepartmentOnly(NewDepartmentEntity target, UUID universityId, String name) {
        NewUniversityEntity newUniversityEntity = getUniversityEntityOrThrowInvalidIndexException(universityId);

        if (doesIndexOfDepartmentChange(target, universityId, name)) {
            checkDepartmentConflict(universityId, name);
        }

        return target
                .withName(name)
                .withUniversity(newUniversityEntity);
    }

    private NewDepartmentEntity saveBaseMajorJoins(NewDepartmentEntity newDepartmentEntity, List<UUID> baseMajorIds) {
        for (UUID baseMajorId : baseMajorIds) {
            NewMajorEntity baseMajorEntity = findMajorEntity(baseMajorId);
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

    private boolean doesIndexOfDepartmentChange(NewDepartmentEntity target, UUID universityId, String name) {
        UUID oldUniversityId = target.getUniversity().getId();
        String oldDepartmentName = target.getName();
        return !(oldUniversityId.equals(universityId)
                && oldDepartmentName.equals(name));
    }

    private void checkDepartmentIdConflict(UUID departmentId) {
        if (departmentRepository.existsById(departmentId)) {
            throw new ConflictException();
        }
    }

    private void checkDepartmentConflict(UUID universityId, String departmentName) {
        if (departmentRepository.existsByUniversityIdAndName(universityId, departmentName)) {
            throw new ConflictException();
        }
    }
}
