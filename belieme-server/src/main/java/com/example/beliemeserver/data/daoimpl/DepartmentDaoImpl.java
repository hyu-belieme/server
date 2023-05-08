package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.MajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.domain.dao.DepartmentDao;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DepartmentDaoImpl extends BaseDaoImpl implements DepartmentDao {

    public DepartmentDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<DepartmentDto> getAllList() {
        List<DepartmentDto> output = new ArrayList<>();

        for (DepartmentEntity departmentEntity : departmentRepository.findAll()) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public List<DepartmentDto> getListByUniversity(@NonNull UUID universityId) {
        List<DepartmentDto> output = new ArrayList<>();

        validateUniversityId(universityId);
        for (DepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
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
        DepartmentEntity newDepartmentEntity = saveDepartmentOnly(departmentId, universityId, name);
        newDepartmentEntity = saveBaseMajorJoins(newDepartmentEntity, majorId);

        return departmentRepository.save(newDepartmentEntity).toDepartmentDto();
    }

    @Override
    public DepartmentDto update(@NonNull UUID departmentId, @NonNull UUID universityId, @NonNull String name, @NonNull List<UUID> majorId) {
        DepartmentEntity target = findDepartmentEntity(departmentId);
        target = updateDepartmentOnly(target, universityId, name);

        target = removeAllBaseMajorJoins(target);
        target = saveBaseMajorJoins(target, majorId);

        return departmentRepository.save(target).toDepartmentDto();
    }

    private DepartmentEntity saveDepartmentOnly(UUID departmentId, UUID universityId, String name) {
        UniversityEntity university = getUniversityEntityOrThrowInvalidIndexException(universityId);

        checkDepartmentIdConflict(departmentId);
        checkDepartmentConflict(university.getId(), name);

        DepartmentEntity newDepartmentEntity = new DepartmentEntity(departmentId, university, name);
        return departmentRepository.save(newDepartmentEntity);
    }

    private DepartmentEntity updateDepartmentOnly(DepartmentEntity target, UUID universityId, String name) {
        UniversityEntity newUniversityEntity = getUniversityEntityOrThrowInvalidIndexException(universityId);

        if (doesIndexOfDepartmentChange(target, universityId, name)) {
            checkDepartmentConflict(universityId, name);
        }

        return target
                .withName(name)
                .withUniversity(newUniversityEntity);
    }

    private DepartmentEntity saveBaseMajorJoins(DepartmentEntity newDepartmentEntity, List<UUID> baseMajorIds) {
        for (UUID baseMajorId : baseMajorIds) {
            MajorEntity baseMajorEntity = findMajorEntity(baseMajorId);
            MajorDepartmentJoinEntity newJoin = new MajorDepartmentJoinEntity(
                    baseMajorEntity,
                    newDepartmentEntity
            );
            MajorDepartmentJoinEntity newMajorJoin = majorDepartmentJoinRepository.save(newJoin);
            newDepartmentEntity = newDepartmentEntity.withBaseMajorAdd(newMajorJoin);
        }
        return newDepartmentEntity;
    }

    private DepartmentEntity removeAllBaseMajorJoins(DepartmentEntity department) {
        majorDepartmentJoinRepository.deleteAll(department.getBaseMajorJoin());
        return department.withBaseMajorClear();
    }

    private boolean doesIndexOfDepartmentChange(DepartmentEntity target, UUID universityId, String name) {
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
