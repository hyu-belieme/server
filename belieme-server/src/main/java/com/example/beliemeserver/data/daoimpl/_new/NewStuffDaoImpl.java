package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.StuffDao;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewStuffDaoImpl extends NewBaseDaoImpl implements StuffDao {
    public NewStuffDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<StuffDto> getAllList() {
        List<StuffDto> output = new ArrayList<>();
        for (NewStuffEntity stuffEntity : stuffRepository.findAll()) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public List<StuffDto> getListByDepartment(@NonNull UUID departmentId) {
        validateDepartmentId(departmentId);

        List<StuffDto> output = new ArrayList<>();
        for (NewStuffEntity stuffEntity : stuffRepository.findByDepartmentId(departmentId)) {
            output.add(stuffEntity.toStuffDto());
        }
        return output;
    }

    @Override
    public StuffDto getById(@NonNull UUID stuffId) {
        return findStuffEntity(stuffId).toStuffDto();
    }

    @Override
    public StuffDto create(@NonNull UUID stuffId, @NonNull UUID departmentId, @NonNull String name, String thumbnail) {
        NewDepartmentEntity departmentOfNewStuff = getDepartmentEntityOrThrowInvalidIndexException(departmentId);

        checkStuffIdConflict(stuffId);
        checkStuffConflict(departmentId, name);

        NewStuffEntity newStuffEntity = new NewStuffEntity(
                stuffId,
                departmentOfNewStuff,
                name,
                thumbnail
        );
        return stuffRepository.save(newStuffEntity).toStuffDto();
    }

    @Override
    public StuffDto update(@NonNull UUID stuffId, @NonNull UUID newDepartmentId, @NonNull String newName, String newThumbnail) {
        NewStuffEntity target = findStuffEntity(stuffId);
        NewDepartmentEntity departmentOfNewStuff = getDepartmentEntityOrThrowInvalidIndexException(newDepartmentId);

        if (doesIndexChange(target, newDepartmentId, newName)) {
            checkStuffConflict(departmentOfNewStuff.getId(), newName);
        }

        target = target.withDepartment(departmentOfNewStuff)
                .withName(newName)
                .withThumbnail(newThumbnail);
        return stuffRepository.save(target).toStuffDto();
    }

    private boolean doesIndexChange(NewStuffEntity target, UUID newDepartmentId, String newName) {
        return !(target.getDepartmentId().equals(newDepartmentId)
                && target.getName().equals(newName));
    }

    private void checkStuffIdConflict(UUID stuffId) {
        if (stuffRepository.existsById(stuffId)) {
            throw new ConflictException();
        }
    }

    private void checkStuffConflict(UUID departmentId, String name) {
        if (stuffRepository.existsByDepartmentIdAndName(departmentId, name)) {
            throw new ConflictException();
        }
    }
}
