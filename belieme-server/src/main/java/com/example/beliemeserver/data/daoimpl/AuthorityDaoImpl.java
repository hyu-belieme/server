package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.domain.dao.AuthorityDao;
import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import com.example.beliemeserver.error.exception.ConflictException;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AuthorityDaoImpl extends BaseDaoImpl implements AuthorityDao {
    public AuthorityDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<AuthorityDto> getAllList() {
        List<AuthorityDto> output = new ArrayList<>();

        for (AuthorityEntity authorityEntity : authorityRepository.findAll()) {
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
        DepartmentEntity departmentOfAuthority = getDepartmentEntityOrThrowInvalidIndexException(departmentId);

        checkAuthorityConflict(departmentOfAuthority.getId(), permission.name());

        AuthorityEntity newAuthority = new AuthorityEntity(
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
