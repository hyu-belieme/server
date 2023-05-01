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
    public MajorDto getByIndex(String universityName, String majorCode) {
        NewMajorEntity targetEntity = findMajorEntity(universityName, majorCode);
        return targetEntity.toMajorDto();
    }

    @Override
    public MajorDto create(MajorDto newMajor) {
        NewUniversityEntity university = findUniversityEntity(newMajor.university().id());

        checkMajorConflict(university.getId(), newMajor.code());

        NewMajorEntity newMajorEntity = new NewMajorEntity(
                newMajor.id(),
                university,
                newMajor.code()
        );

        NewMajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto update(String universityName, String majorCode, MajorDto newMajor) {
        NewMajorEntity target = findMajorEntity(universityName, majorCode);

        NewUniversityEntity newUniversity = findUniversityEntity(newMajor.university().id());

        checkMajorConflict(newUniversity.getId(), newMajor.code());

        target.setUniversity(newUniversity)
                .setCode(newMajor.code());

        return target.toMajorDto();
    }

    @Override
    public MajorDto update(UUID majorId, MajorDto newMajor) {
        NewMajorEntity target = findMajorEntity(majorId);

        NewUniversityEntity newUniversity = findUniversityEntity(newMajor.university().id());

        if (doesIndexChange(target, newMajor)) {
            checkMajorConflict(newUniversity.getId(), newMajor.code());
        }

        target.setUniversity(newUniversity)
                .setCode(newMajor.code());

        return target.toMajorDto();
    }

    private boolean doesIndexChange(NewMajorEntity target, MajorDto newMajor) {
        UUID oldUniversityId = target.getUniversity().getId();
        String oldCode = target.getCode();

        return !(oldUniversityId.equals(newMajor.university().id())
                && oldCode.equals(newMajor.code()));
    }

    private void checkMajorConflict(UUID universityId, String majorCode) {
        if (majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }
    }
}
