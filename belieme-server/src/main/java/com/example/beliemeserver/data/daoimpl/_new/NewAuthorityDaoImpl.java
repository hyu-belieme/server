package com.example.beliemeserver.data.daoimpl._new;

import com.example.beliemeserver.data.entity._new.NewAuthorityEntity;
import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.repository._new.*;
import com.example.beliemeserver.domain.dao._new.AuthorityDao;
import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
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
    public boolean checkExistByIndex(@NonNull UUID departmentId, @NonNull Permission permission) {
        return authorityRepository.existsByDepartmentIdAndPermission(departmentId, permission.toString());
    }

    @Override
    public AuthorityDto create(@NonNull UUID departmentId, @NonNull Permission permission) {
        NewDepartmentEntity departmentOfAuthority = findDepartmentEntity(departmentId);

        checkAuthorityConflict(departmentOfAuthority.getId(), permission.name());

        NewAuthorityEntity newAuthority = new NewAuthorityEntity(
                departmentOfAuthority,
                permission.name()
        );
        return authorityRepository.save(newAuthority).toAuthorityDto();
    }

    private void checkAuthorityConflict(UUID departmentId, String permission) {
        if (authorityRepository.existsByDepartmentIdAndPermission(departmentId, permission)) {
            throw new ConflictException();
        }
    }
}
