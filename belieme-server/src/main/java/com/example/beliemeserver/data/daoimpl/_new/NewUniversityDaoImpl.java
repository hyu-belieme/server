package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.UniversityDao;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import com.example.beliemeserver.error.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewUniversityDaoImpl extends NewBaseDaoImpl implements UniversityDao {

    @Autowired
    public NewUniversityDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<UniversityDto> getAllList() {
        List<UniversityDto> output = new ArrayList<>();

        for (NewUniversityEntity universityEntity : universityRepository.findAll()) {
            output.add(universityEntity.toUniversityDto());
        }
        return output;
    }

    @Override
    public UniversityDto getByIndex(String universityName) {
        NewUniversityEntity targetUniversity = findUniversityEntity(universityName);
        return targetUniversity.toUniversityDto();
    }

    @Override
    public UniversityDto getById(UUID id) {
        NewUniversityEntity target = findUniversityEntity(id);
        return target.toUniversityDto();
    }

    @Override
    public boolean checkExistById(UUID id) {
        return universityRepository.existsById(id);
    }

    @Override
    public UniversityDto create(UniversityDto newUniversity) {
        checkUniversityConflict(newUniversity.name());

        NewUniversityEntity newUniversityEntity = new NewUniversityEntity(
                newUniversity.id(),
                newUniversity.name(),
                newUniversity.apiUrl()
        );

        NewUniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
        return savedUniversityEntity.toUniversityDto();
    }

    @Override
    public UniversityDto update(String universityName, UniversityDto newUniversity) {
        NewUniversityEntity target = findUniversityEntity(universityName);
        if (doesIndexOfUniversityChange(target, newUniversity)) {
            checkUniversityConflict(newUniversity.name());
        }

        NewUniversityEntity updatedUniversity = target
                .withName(newUniversity.name())
                .withApiUrl(newUniversity.apiUrl());

        return universityRepository.save(updatedUniversity).toUniversityDto();
    }

    @Override
    public UniversityDto update(UUID id, UniversityDto newUniversity) {
        NewUniversityEntity target = findUniversityEntity(id);
        if (doesIndexOfUniversityChange(target, newUniversity)) {
            checkUniversityConflict(newUniversity.name());
        }

        NewUniversityEntity updatedUniversity = target
                .withName(newUniversity.name())
                .withApiUrl(newUniversity.apiUrl());

        return universityRepository.save(updatedUniversity).toUniversityDto();
    }

    private boolean doesIndexOfUniversityChange(NewUniversityEntity oldUniversity, UniversityDto newUniversity) {
        return !oldUniversity.getName().equals(newUniversity.name());
    }

    private void checkUniversityConflict(String universityName) {
        if (universityRepository.existsByName(universityName)) {
            throw new ConflictException();
        }
    }
}
