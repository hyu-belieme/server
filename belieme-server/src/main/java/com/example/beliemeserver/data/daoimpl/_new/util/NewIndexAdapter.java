package com.example.beliemeserver.data.daoimpl._new.util;

import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.error.exception.NotFoundException;

import java.util.UUID;

public class NewIndexAdapter {
    public static NewUniversityEntity getUniversityEntity(NewUniversityRepository universityRepository, String name) {
        NewUniversityEntity target = universityRepository.findByName(name).orElse(null);
        return (NewUniversityEntity) checkNullAndReturn(target);
    }

    public static NewDepartmentEntity getDepartmentEntity(NewDepartmentRepository departmentRepository, UUID universityId, String departmentName) {
        NewDepartmentEntity target = departmentRepository.findByUniversityIdAndName(universityId, departmentName).orElse(null);
        return (NewDepartmentEntity) checkNullAndReturn(target);
    }

    public static NewUserEntity getUserEntity(NewUserRepository userRepository, UUID universityId, String studentId) {
        NewUserEntity target = userRepository.findByUniversityIdAndStudentId(universityId, studentId).orElse(null);
        return (NewUserEntity) checkNullAndReturn(target);
    }

    public static NewUserEntity getUserEntityByToken(NewUserRepository userRepository, String token) {
        NewUserEntity target = userRepository.findByToken(token).orElse(null);
        return (NewUserEntity) checkNullAndReturn(target);
    }

    public static NewMajorEntity getMajorEntity(NewMajorRepository majorRepository, UUID universityId, String majorCode) {
        NewMajorEntity target = majorRepository.findByUniversityIdAndCode(universityId, majorCode).orElse(null);
        return (NewMajorEntity) checkNullAndReturn(target);
    }

    public static NewAuthorityEntity getAuthorityEntity(NewAuthorityRepository authorityRepository, UUID departmentId, String permission) {
        NewAuthorityEntity target = authorityRepository.findByDepartmentIdAndPermission(departmentId, permission).orElse(null);
        return (NewAuthorityEntity) checkNullAndReturn(target);
    }

    public static NewStuffEntity getStuffEntity(NewStuffRepository stuffRepository, UUID departmentId, String stuffName) {
        NewStuffEntity target = stuffRepository.findByDepartmentIdAndName(departmentId, stuffName).orElse(null);
        return (NewStuffEntity) checkNullAndReturn(target);
    }

    public static NewItemEntity getItemEntity(NewItemRepository itemRepository, UUID stuffId, int num) {
        NewItemEntity target = itemRepository.findByStuffIdAndNum(stuffId, num).orElse(null);
        return (NewItemEntity) checkNullAndReturn(target);
    }

    public static NewHistoryEntity getHistoryEntity(NewHistoryRepository historyRepository, UUID itemId, int num) {
        NewHistoryEntity target = historyRepository.findByItemIdAndNum(itemId, num).orElse(null);
        return (NewHistoryEntity) checkNullAndReturn(target);
    }

    public static NewUniversityEntity getUniversityEntity(NewUniversityRepository universityRepository, UUID id) {
        NewUniversityEntity target = universityRepository.findById(id).orElse(null);
        return (NewUniversityEntity) checkNullAndReturn(target);
    }

    public static NewDepartmentEntity getDepartmentEntity(NewDepartmentRepository departmentRepository, UUID id) {
        NewDepartmentEntity target = departmentRepository.findById(id).orElse(null);
        return (NewDepartmentEntity) checkNullAndReturn(target);
    }

    public static NewUserEntity getUserEntity(NewUserRepository userRepository, UUID id) {
        NewUserEntity target = userRepository.findById(id).orElse(null);
        return (NewUserEntity) checkNullAndReturn(target);
    }

    public static NewMajorEntity getMajorEntity(NewMajorRepository majorRepository, UUID id) {
        NewMajorEntity target = majorRepository.findById(id).orElse(null);
        return (NewMajorEntity) checkNullAndReturn(target);
    }

    public static NewStuffEntity getStuffEntity(NewStuffRepository stuffRepository, UUID id) {
        NewStuffEntity target = stuffRepository.findById(id).orElse(null);
        return (NewStuffEntity) checkNullAndReturn(target);
    }

    public static NewItemEntity getItemEntity(NewItemRepository itemRepository, UUID id) {
        NewItemEntity target = itemRepository.findById(id).orElse(null);
        return (NewItemEntity) checkNullAndReturn(target);
    }

    public static NewHistoryEntity getHistoryEntity(NewHistoryRepository historyRepository, UUID id) {
        NewHistoryEntity target = historyRepository.findById(id).orElse(null);
        return (NewHistoryEntity) checkNullAndReturn(target);
    }

    private static <T> NewDataEntity<T> checkNullAndReturn(NewDataEntity<T> target) {
        if (target == null) {
            throw new NotFoundException();
        }
        return target;
    }
}
