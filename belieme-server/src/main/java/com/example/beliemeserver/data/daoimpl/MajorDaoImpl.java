package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.domain.dao.MajorDao;
import com.example.beliemeserver.domain.dto.MajorDto;
import com.example.beliemeserver.error.exception.ConflictException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MajorDaoImpl extends BaseDaoImpl implements MajorDao {

    public MajorDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<MajorDto> getAllList() {
        List<MajorDto> output = new ArrayList<>();
        for (MajorEntity majorEntity : majorRepository.findAll()) {
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
        MajorEntity targetEntity = findMajorEntity(universityId, majorCode);
        return targetEntity.toMajorDto();
    }

    @Override
    public MajorDto create(UUID majorId, UUID universityId, String majorCode) {
        UniversityEntity university;
        university = getUniversityEntityOrThrowInvalidIndexException(universityId);

        checkMajorIdConflict(majorId);
        checkMajorConflict(university.getId(), majorCode);

        MajorEntity newMajorEntity = new MajorEntity(
                majorId,
                university,
                majorCode
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto update(UUID majorId, UUID universityId, String majorCode) {
        MajorEntity target = findMajorEntity(majorId);
        UniversityEntity newUniversity = getUniversityEntityOrThrowInvalidIndexException(universityId);

        if (doesIndexChange(target, universityId, majorCode)) {
            checkMajorConflict(newUniversity.getId(), majorCode);
        }

        MajorEntity updatedMajor = target
                .withUniversity(newUniversity)
                .withCode(majorCode);

        return majorRepository.save(updatedMajor).toMajorDto();
    }

    private boolean doesIndexChange(MajorEntity target, UUID newUniversityId, String newMajorCode) {
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
