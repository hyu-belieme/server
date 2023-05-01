package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewAuthorityEntity;
import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.AuthorityDao;
import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class NewAuthorityDaoImpl extends NewBaseDaoImpl implements AuthorityDao {
    public NewAuthorityDaoImpl(NewUniversityRepository universityRepository, NewDepartmentRepository departmentRepository, NewUserRepository userRepository, NewMajorRepository majorRepository, NewMajorDepartmentJoinRepository majorDepartmentJoinRepository, NewAuthorityRepository authorityRepository, NewAuthorityUserJoinRepository authorityUserJoinRepository, NewStuffRepository stuffRepository, NewItemRepository itemRepository, NewHistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<AuthorityDto> getAllList() {
        List<AuthorityDto> output = new ArrayList<>();

        for (NewAuthorityEntity authorityEntity : authorityRepository.findAll()) {
            output.add(authorityEntity.toAuthorityDto());
        }
        return output;
    }

    @Override
    public boolean checkExistByIndex(String universityName, String departmentName, Permission permission) {
        try {
            UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();
            return authorityRepository.existsByDepartmentIdAndPermission(departmentId, permission.toString());
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public AuthorityDto create(AuthorityDto authority) {
        NewDepartmentEntity departmentOfAuthority = findDepartmentEntity(authority.department());

        checkAuthorityConflict(departmentOfAuthority.getId(), authority.permission().name());

        NewAuthorityEntity newAuthority = new NewAuthorityEntity(
                departmentOfAuthority,
                authority.permission().name()
        );

        return authorityRepository.save(newAuthority).toAuthorityDto();
    }

    @Override
    public AuthorityDto update(String universityName, String departmentName, Permission permission, AuthorityDto newAuthority) {
        NewAuthorityEntity target = findAuthorityEntity(universityName, departmentName, permission.name());
        NewDepartmentEntity departmentOfAuthority = findDepartmentEntity(newAuthority.department());

        if (doesIndexChange(target, newAuthority)) {
            checkAuthorityConflict(departmentOfAuthority.getId(), newAuthority.permission().name());
        }

        target.setDepartment(departmentOfAuthority)
                .setPermission(newAuthority.permission().name());
        return target.toAuthorityDto();
    }

    private boolean doesIndexChange(NewAuthorityEntity target, AuthorityDto newAuthority) {
        String oldUniversityName = target.getDepartment().getUniversity().getName();
        String oldDepartmentName = target.getDepartment().getName();
        String oldPermission = target.getPermission();

        return !(oldUniversityName.equals(newAuthority.department().university().name()) &&
                oldDepartmentName.equals(newAuthority.department().name()) &&
                oldPermission.equals(newAuthority.permission().name()));
    }

    private void checkAuthorityConflict(UUID departmentId, String permission) {
        if (authorityRepository.existsByDepartmentIdAndPermission(departmentId, permission)) {
            throw new ConflictException();
        }
    }
}
