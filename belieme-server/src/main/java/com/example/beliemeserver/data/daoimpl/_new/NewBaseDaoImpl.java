package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.daoimpl._new.util.NewIndexAdapter;
import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dto._new.*;
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

    protected NewUniversityEntity findUniversityEntity(String universityName) {
        return NewIndexAdapter.getUniversityEntity(universityRepository, universityName);
    }

    protected NewUniversityEntity findUniversityEntity(UniversityDto universityDto) {
        String universityName = universityDto.name();
        return findUniversityEntity(universityName);
    }

    protected NewDepartmentEntity findDepartmentEntity(String universityName, String departmentName) {
        UUID universityId = findUniversityEntity(universityName).getId();

        return NewIndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentName);
    }

    protected NewDepartmentEntity findDepartmentEntity(DepartmentDto departmentDto) {
        String universityName = departmentDto.university().name();
        String departmentName = departmentDto.name();

        return findDepartmentEntity(universityName, departmentName);
    }

    protected NewUserEntity findUserEntity(String universityName, String studentId) {
        UUID universityId = findUniversityEntity(universityName).getId();

        return NewIndexAdapter.getUserEntity(userRepository, universityId, studentId);
    }

    protected NewUserEntity findUserEntity(UserDto userDto) {
        String universityName = userDto.university().name();
        String studentId = userDto.studentId();

        return findUserEntity(universityName, studentId);
    }

    protected NewUserEntity findUserEntityByToken(String token) {
        return NewIndexAdapter.getUserEntityByToken(userRepository, token);
    }

    protected NewMajorEntity findMajorEntity(String universityName, String majorCode) {
        UUID universityId = findUniversityEntity(universityName).getId();

        return NewIndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
    }

    protected NewMajorEntity findMajorEntity(MajorDto majorDto) {
        String universityName = majorDto.university().name();
        String majorCode = majorDto.code();

        return findMajorEntity(universityName, majorCode);
    }

    protected NewAuthorityEntity findAuthorityEntity(String universityName, String departmentName, String permission) {
        UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();

        return NewIndexAdapter.getAuthorityEntity(authorityRepository, departmentId, permission);
    }

    protected NewAuthorityEntity findAuthorityEntity(AuthorityDto authorityDto) {
        String universityName = authorityDto.department().university().name();
        String departmentName = authorityDto.department().name();
        String permission = authorityDto.permission().toString();

        return findAuthorityEntity(universityName, departmentName, permission);
    }

    protected NewStuffEntity findStuffEntity(String universityName, String departmentName, String stuffName) {
        UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();

        return NewIndexAdapter.getStuffEntity(stuffRepository, departmentId, stuffName);
    }

    protected NewStuffEntity findStuffEntity(StuffDto stuffDto) {
        String universityName = stuffDto.department().university().name();
        String departmentName = stuffDto.department().name();
        String stuffName = stuffDto.name();

        return findStuffEntity(universityName, departmentName, stuffName);
    }

    protected NewItemEntity findItemEntity(String universityName, String departmentName, String stuffName, int itemNum) {
        UUID stuffId = findStuffEntity(universityName, departmentName, stuffName).getId();

        return NewIndexAdapter.getItemEntity(itemRepository, stuffId, itemNum);
    }

    protected NewItemEntity findItemEntity(ItemDto itemDto) {
        String universityName = itemDto.stuff().department().university().name();
        String departmentName = itemDto.stuff().department().name();
        String stuffName = itemDto.stuff().name();
        int itemNum = itemDto.num();

        return findItemEntity(universityName, departmentName, stuffName, itemNum);
    }

    protected NewHistoryEntity findHistoryEntity(String universityName, String departmentName, String stuffName, int itemNum, int historyNum) {
        UUID itemId = findItemEntity(universityName, departmentName, stuffName, itemNum).getId();

        return NewIndexAdapter.getHistoryEntity(historyRepository, itemId, historyNum);
    }

    protected NewHistoryEntity findHistoryEntity(HistoryDto historyDto) {
        String universityName = historyDto.item().stuff().department().university().name();
        String departmentName = historyDto.item().stuff().department().name();
        String stuffName = historyDto.item().stuff().name();
        int itemNum = historyDto.item().num();
        int historyNum = historyDto.num();

        return findHistoryEntity(universityName, departmentName, stuffName, itemNum, historyNum);
    }
}
