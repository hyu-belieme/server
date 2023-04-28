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
public class MajorDaoImpl extends NewBaseDaoImpl implements MajorDao {

    public MajorDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
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
    public MajorDto getByIndex(String universityName, String majorCode) {
        NewMajorEntity targetEntity = findMajorEntity(universityName, majorCode);
        return targetEntity.toMajorDto();
    }

    @Override
    public MajorDto create(MajorDto newMajor) {
        NewUniversityEntity university = findUniversityEntity(newMajor.university());

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

        NewUniversityEntity newUniversity = findUniversityEntity(newMajor.university());

        checkMajorConflict(newUniversity.getId(), newMajor.code());

        target.setUniversity(newUniversity)
                .setCode(newMajor.code());

        return target.toMajorDto();
    }

    private void checkMajorConflict(UUID universityId, String majorCode) {
        if (majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }
    }
}