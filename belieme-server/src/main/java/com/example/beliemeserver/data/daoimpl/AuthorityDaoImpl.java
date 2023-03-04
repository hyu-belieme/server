package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.AuthorityDao;
import com.example.beliemeserver.model.dto.AuthorityDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    public boolean checkExistByIndex(String universityCode, String departmentCode, AuthorityDto.Permission permission) {
        try {
            int departmentId = findDepartmentEntity(universityCode, departmentCode).getId();
            return authorityRepository.existsByDepartmentIdAndPermission(departmentId, permission.toString());
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public AuthorityDto create(AuthorityDto authority) {
        DepartmentEntity departmentOfAuthority = findDepartmentEntity(authority.department());

        checkAuthorityConflict(departmentOfAuthority.getId(), authority.permission().name());

        AuthorityEntity newAuthority = new AuthorityEntity(
                departmentOfAuthority,
                authority.permission().name()
        );

        return authorityRepository.save(newAuthority).toAuthorityDto();
    }

    @Override
    public AuthorityDto update(String universityCode, String departmentCode, AuthorityDto.Permission permission, AuthorityDto newAuthority) {
        AuthorityEntity target = findAuthorityEntity(universityCode, departmentCode, permission.name());
        DepartmentEntity departmentOfAuthority = findDepartmentEntity(newAuthority.department());

        if (doesIndexChange(target, newAuthority)) {
            checkAuthorityConflict(departmentOfAuthority.getId(), newAuthority.permission().name());
        }

        target.setDepartment(departmentOfAuthority)
                .setPermission(newAuthority.permission().name());
        return target.toAuthorityDto();
    }

    private boolean doesIndexChange(AuthorityEntity target, AuthorityDto newAuthority) {
        String oldUniversityCode = target.getDepartment().getUniversity().getCode();
        String oldDepartmentCode = target.getDepartment().getCode();
        String oldPermission = target.getPermission();

        return !(oldUniversityCode.equals(newAuthority.department().university().code()) &&
                oldDepartmentCode.equals(newAuthority.department().code()) &&
                oldPermission.equals(newAuthority.permission().name()));
    }

    private void checkAuthorityConflict(int departmentId, String permission) {
        if (authorityRepository.existsByDepartmentIdAndPermission(departmentId, permission)) {
            throw new ConflictException();
        }
    }
}
