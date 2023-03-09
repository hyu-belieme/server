package com.example.beliemeserver.data.daoimpl.util;

import com.example.beliemeserver.data.entity.*;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.error.exception.NotFoundException;

public class IndexAdapter {
    public static UniversityEntity getUniversityEntity(UniversityRepository universityRepository, String code) {
        UniversityEntity target = universityRepository.findByCode(code).orElse(null);
        return (UniversityEntity) checkNullAndReturn(target);
    }

    public static DepartmentEntity getDepartmentEntity(DepartmentRepository departmentRepository, int universityId, String departmentCode) {
        DepartmentEntity target = departmentRepository.findByUniversityIdAndCode(universityId, departmentCode).orElse(null);
        return (DepartmentEntity) checkNullAndReturn(target);
    }

    public static UserEntity getUserEntity(UserRepository userRepository, int universityId, String studentId) {
        UserEntity target = userRepository.findByUniversityIdAndStudentId(universityId, studentId).orElse(null);
        return (UserEntity) checkNullAndReturn(target);
    }

    public static UserEntity getUserEntityByToken(UserRepository userRepository, String token) {
        UserEntity target = userRepository.findByToken(token).orElse(null);
        return (UserEntity) checkNullAndReturn(target);
    }

    public static MajorEntity getMajorEntity(MajorRepository majorRepository, int universityId, String majorCode) {
        MajorEntity target = majorRepository.findByUniversityIdAndCode(universityId, majorCode).orElse(null);
        return (MajorEntity) checkNullAndReturn(target);
    }

    public static AuthorityEntity getAuthorityEntity(AuthorityRepository authorityRepository, int departmentId, String permission) {
        AuthorityEntity target = authorityRepository.findByDepartmentIdAndPermission(departmentId, permission).orElse(null);
        return (AuthorityEntity) checkNullAndReturn(target);
    }

    public static StuffEntity getStuffEntity(StuffRepository stuffRepository, int departmentId, String stuffName) {
        StuffEntity target = stuffRepository.findByDepartmentIdAndName(departmentId, stuffName).orElse(null);
        return (StuffEntity) checkNullAndReturn(target);
    }

    public static ItemEntity getItemEntity(ItemRepository itemRepository, int stuffId, int num) {
        ItemEntity target = itemRepository.findByStuffIdAndNum(stuffId, num).orElse(null);
        return (ItemEntity) checkNullAndReturn(target);
    }

    public static HistoryEntity getHistoryEntity(HistoryRepository historyRepository, int itemId, int num) {
        HistoryEntity target = historyRepository.findByItemIdAndNum(itemId, num).orElse(null);
        return (HistoryEntity) checkNullAndReturn(target);
    }

    private static DataEntity checkNullAndReturn(DataEntity target) {
        if (target == null) {
            throw new NotFoundException();
        }
        return target;
    }
}
