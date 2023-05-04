package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewMajorEntity;
import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.MajorDao;
import com.example.beliemeserver.domain.dto._new.MajorDto;
import com.example.beliemeserver.error.exception.ConflictException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewMajorDaoImpl extends NewBaseDaoImpl implements MajorDao {

    public NewMajorDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<MajorDto> getAllList() {
        List<MajorDto> output = new ArrayList<>();
        for (NewMajorEntity majorEntity : majorRepository.findAll()) {
            output.add(majorEntity.toMajorDto());
        }
        return output;
    }

    @Override
    public MajorDto getById(UUID majorId) {
        return findMajorEntity(majorId).toMajorDto();
    }

    @Override
    public MajorDto getByIndex(UUID universityId, String majorCode) {
        NewMajorEntity targetEntity = findMajorEntity(universityId, majorCode);
        return targetEntity.toMajorDto();
    }

    @Override
    public MajorDto create(UUID majorId, UUID universityId, String majorCode) {
        NewUniversityEntity university = findUniversityEntity(universityId);

        checkMajorIdConflict(majorId);
        checkMajorConflict(university.getId(), majorCode);

        NewMajorEntity newMajorEntity = new NewMajorEntity(
                majorId,
                university,
                majorCode
        );

        NewMajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto update(UUID majorId, UUID universityId, String majorCode) {
        NewMajorEntity target = findMajorEntity(majorId);
        NewUniversityEntity newUniversity = findUniversityEntity(universityId);

        if (doesIndexChange(target, universityId, majorCode)) {
            checkMajorConflict(newUniversity.getId(), majorCode);
        }

        NewMajorEntity updatedMajor = target
                .withUniversity(newUniversity)
                .withCode(majorCode);

        return majorRepository.save(updatedMajor).toMajorDto();
    }

    private boolean doesIndexChange(NewMajorEntity target, UUID newUniversityId, String newMajorCode) {
        UUID oldUniversityId = target.getUniversity().getId();
        String oldCode = target.getCode();

        return !(oldUniversityId.equals(newUniversityId) && oldCode.equals(newMajorCode));
    }

    private void checkMajorIdConflict(UUID majorId) {
        if (majorRepository.existsById(majorId)) {
            throw new ConflictException();
        }
    }

    private void checkMajorConflict(UUID universityId, String majorCode) {
        if (majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }
    }
}
