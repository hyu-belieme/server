package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.daoimpl._new.util.NewIndexAdapter;
import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.data.repository._new.*;
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

    protected NewMajorEntity findMajorEntity(String universityName, String majorCode) {
        UUID universityId = findUniversityEntity(universityName).getId();

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
}
