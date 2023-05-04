package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.UniversityDao;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
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
    public UniversityDto getById(@NonNull UUID id) {
        NewUniversityEntity target = findUniversityEntity(id);
        return target.toUniversityDto();
    }

    @Override
    public boolean checkExistById(@NonNull UUID id) {
        return universityRepository.existsById(id);
    }

    @Override
    public UniversityDto create(@NonNull UUID id, @NonNull String name, String apiUrl) {
        checkUniversityIdConflict(id);
        checkUniversityConflict(name);

        NewUniversityEntity newUniversityEntity = new NewUniversityEntity(id, name, apiUrl);

        NewUniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
        return savedUniversityEntity.toUniversityDto();
    }

    @Override
    public UniversityDto update(@NonNull UUID id, @NonNull String name, String apiUrl) {
        NewUniversityEntity target = findUniversityEntity(id);
        if (doesIndexOfUniversityChange(target, name)) {
            checkUniversityConflict(name);
        }

        NewUniversityEntity updatedUniversity = target
                .withName(name)
                .withApiUrl(apiUrl);

        return universityRepository.save(updatedUniversity).toUniversityDto();
    }

    private boolean doesIndexOfUniversityChange(NewUniversityEntity oldUniversity, String universityName) {
        return !oldUniversity.getName().equals(universityName);
    }

    private void checkUniversityIdConflict(UUID universityId) {
        if (universityRepository.existsById(universityId)) {
            throw new ConflictException();
        }
    }

    private void checkUniversityConflict(String universityName) {
        if (universityRepository.existsByName(universityName)) {
            throw new ConflictException();
        }
    }
}
