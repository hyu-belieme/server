package com.belieme.apiserver.data.daoimpl;

import com.belieme.apiserver.data.entity.AuthorityEntity;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.repository.AuthorityRepository;
import com.belieme.apiserver.data.repository.AuthorityUserJoinRepository;
import com.belieme.apiserver.data.repository.DepartmentRepository;
import com.belieme.apiserver.data.repository.HistoryRepository;
import com.belieme.apiserver.data.repository.ItemRepository;
import com.belieme.apiserver.data.repository.MajorDepartmentJoinRepository;
import com.belieme.apiserver.data.repository.MajorRepository;
import com.belieme.apiserver.data.repository.StuffRepository;
import com.belieme.apiserver.data.repository.UniversityRepository;
import com.belieme.apiserver.data.repository.UserRepository;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.error.exception.ConflictException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AuthorityDaoImpl extends BaseDaoImpl implements AuthorityDao {

  public AuthorityDaoImpl(
      UniversityRepository universityRepository, DepartmentRepository departmentRepository,
      UserRepository userRepository, MajorRepository majorRepository,
      MajorDepartmentJoinRepository majorDepartmentJoinRepository,
      AuthorityRepository authorityRepository,
      AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository,
      ItemRepository itemRepository, HistoryRepository historyRepository) {
    super(universityRepository, departmentRepository, userRepository, majorRepository,
        majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository,
        stuffRepository, itemRepository, historyRepository);
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
    return authorityRepository.existsByDepartmentIdAndPermission(departmentId,
        permission.toString());
  }

  @Override
  public AuthorityDto create(@NonNull UUID departmentId, @NonNull Permission permission) {
    DepartmentEntity departmentOfAuthority = getDepartmentEntityOrThrowInvalidIndexException(
        departmentId);

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
