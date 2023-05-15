package com.belieme.apiserver.data.daoimpl;

import com.belieme.apiserver.data.entity.UniversityEntity;
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
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.error.exception.ConflictException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniversityDaoImpl extends BaseDaoImpl implements UniversityDao {

  @Autowired
  public UniversityDaoImpl(
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
  public List<UniversityDto> getAllList() {
    List<UniversityDto> output = new ArrayList<>();

    for (UniversityEntity universityEntity : universityRepository.findAll()) {
      output.add(universityEntity.toUniversityDto());
    }
    return output;
  }

  @Override
  public UniversityDto getById(@NonNull UUID id) {
    UniversityEntity target = findUniversityEntity(id);
    return target.toUniversityDto();
  }

  @Override
  public boolean checkExistById(@NonNull UUID id) {
    return universityRepository.existsById(id);
  }

  @Override
  public UniversityDto create(@NonNull UUID id, @NonNull String name, String apiUrl) {
    checkUniversityIdConflict(id);
    checkUniversityConflict(name);

    UniversityEntity newUniversityEntity = new UniversityEntity(id, name, apiUrl);

    UniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
    return savedUniversityEntity.toUniversityDto();
  }

  @Override
  public UniversityDto update(@NonNull UUID id, @NonNull String name, String apiUrl) {
    UniversityEntity target = findUniversityEntity(id);
    if (doesIndexOfUniversityChange(target, name)) {
      checkUniversityConflict(name);
    }

    UniversityEntity updatedUniversity = target
        .withName(name)
        .withApiUrl(apiUrl);

    return universityRepository.save(updatedUniversity).toUniversityDto();
  }

  private boolean doesIndexOfUniversityChange(UniversityEntity oldUniversity,
      String universityName) {
    return !oldUniversity.getName().equals(universityName);
  }

  private void checkUniversityIdConflict(UUID universityId) {
    if (universityRepository.existsById(universityId)) {
      throw new ConflictException();
    }
  }

  private void checkUniversityConflict(String universityName) {
    if (universityRepository.existsByName(universityName)) {
      throw new ConflictException();
    }
  }
}
