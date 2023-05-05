package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.daoimpl._new.util.NewIndexAdapter;
import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public abstract class NewBaseDaoImpl {
    protected final NewUniversityRepository universityRepository;
    protected final NewDepartmentRepository departmentRepository;
    protected final NewUserRepository userRepository;
    protected final NewMajorRepository majorRepository;
    protected final NewMajorDepartmentJoinRepository majorDepartmentJoinRepository;
    protected final NewAuthorityRepository authorityRepository;
    protected final NewAuthorityUserJoinRepository authorityUserJoinRepository;
    protected final NewStuffRepository stuffRepository;
    protected final NewItemRepository itemRepository;
    protected final NewHistoryRepository historyRepository;

    @Autowired
    public NewBaseDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        this.universityRepository = universityRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.majorRepository = majorRepository;
        this.majorDepartmentJoinRepository = majorDepartmentJoinRepository;
        this.authorityRepository = authorityRepository;
        this.authorityUserJoinRepository = authorityUserJoinRepository;
        this.stuffRepository = stuffRepository;
        this.itemRepository = itemRepository;
        this.historyRepository = historyRepository;
    }

    protected void validateUniversityId(UUID universityId) {
        if(!universityRepository.existsById(universityId)) throw new InvalidIndexException();
    }

    protected void validateDepartmentId(UUID departmentId) {
        if(!departmentRepository.existsById(departmentId)) throw new InvalidIndexException();
    }

    protected void validateUserId(UUID userId) {
        if(!userRepository.existsById(userId)) throw new InvalidIndexException();
    }

    protected void validateStuffId(UUID stuffId) {
        if(!stuffRepository.existsById(stuffId)) throw new InvalidIndexException();
    }

    protected void validateItemId(UUID itemId) {
        if(!itemRepository.existsById(itemId)) throw new InvalidIndexException();
    }

    protected NewUniversityEntity findUniversityEntity(UUID id) {
        return NewIndexAdapter.getUniversityEntity(universityRepository, id);
    }

    protected NewUniversityEntity findUniversityEntity(String universityName) {
        return NewIndexAdapter.getUniversityEntity(universityRepository, universityName);
    }

    protected NewDepartmentEntity findDepartmentEntity(UUID id) {
        return NewIndexAdapter.getDepartmentEntity(departmentRepository, id);
    }

    protected NewDepartmentEntity findDepartmentEntity(String universityName, String departmentName) {
        UUID universityId = findUniversityEntity(universityName).getId();

        return NewIndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentName);
    }

    protected NewUserEntity findUserEntity(UUID id) {
        return NewIndexAdapter.getUserEntity(userRepository, id);
    }

    protected NewUserEntity findUserEntity(UUID universityId, String studentId) {
        return NewIndexAdapter.getUserEntity(userRepository, universityId, studentId);
    }

    protected NewUserEntity findUserEntity(String universityName, String studentId) {
        UUID universityId = findUniversityEntity(universityName).getId();

        return NewIndexAdapter.getUserEntity(userRepository, universityId, studentId);
    }

    protected NewUserEntity findUserEntityByToken(String token) {
        return NewIndexAdapter.getUserEntityByToken(userRepository, token);
    }

    protected NewMajorEntity findMajorEntity(UUID id) {
        return NewIndexAdapter.getMajorEntity(majorRepository, id);
    }

    protected NewMajorEntity findMajorEntity(UUID universityId, String majorCode) {
        return NewIndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
    }

    protected NewAuthorityEntity findAuthorityEntity(UUID departmentId, String permission) {
        return NewIndexAdapter.getAuthorityEntity(authorityRepository, departmentId, permission);
    }

    protected NewAuthorityEntity findAuthorityEntity(String universityName, String departmentName, String permission) {
        UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();

        return NewIndexAdapter.getAuthorityEntity(authorityRepository, departmentId, permission);
    }

    protected NewStuffEntity findStuffEntity(UUID stuffId) {
        return NewIndexAdapter.getStuffEntity(stuffRepository, stuffId);
    }

    protected NewStuffEntity findStuffEntity(String universityName, String departmentName, String stuffName) {
        UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();

        return NewIndexAdapter.getStuffEntity(stuffRepository, departmentId, stuffName);
    }

    protected NewItemEntity findItemEntity(UUID itemId) {
        return NewIndexAdapter.getItemEntity(itemRepository, itemId);
    }

    protected NewItemEntity findItemEntity(String universityName, String departmentName, String stuffName, int itemNum) {
        UUID stuffId = findStuffEntity(universityName, departmentName, stuffName).getId();

        return NewIndexAdapter.getItemEntity(itemRepository, stuffId, itemNum);
    }

    protected NewHistoryEntity findHistoryEntity(UUID historyId) {
        return NewIndexAdapter.getHistoryEntity(historyRepository, historyId);
    }

    protected NewHistoryEntity findHistoryEntity(String universityName, String departmentName, String stuffName, int itemNum, int historyNum) {
        UUID itemId = findItemEntity(universityName, departmentName, stuffName, itemNum).getId();

        return NewIndexAdapter.getHistoryEntity(historyRepository, itemId, historyNum);
    }

    protected NewUniversityEntity getUniversityEntityOrThrowInvalidIndexException(UUID universityId) {
        try {
            return findUniversityEntity(universityId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected NewDepartmentEntity getDepartmentEntityOrThrowInvalidIndexException(UUID departmentId) {
        try {
            return findDepartmentEntity(departmentId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected NewUserEntity getUserEntityOrThrowInvalidIndexException(UUID userId) {
        try {
            return findUserEntity(userId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected NewStuffEntity getStuffEntityOrThrowInvalidIndexException(UUID stuffId) {
        try {
            return findStuffEntity(stuffId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected NewItemEntity getItemEntityOrThrowInvalidIndexException(UUID itemId) {
        try {
            return findItemEntity(itemId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected NewHistoryEntity getHistoryEntityOrThrowInvalidIndexException(UUID historyId) {
        try {
            return findHistoryEntity(historyId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }
}
