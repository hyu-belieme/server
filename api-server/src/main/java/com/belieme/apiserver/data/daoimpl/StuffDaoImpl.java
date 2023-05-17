package com.belieme.apiserver.data.daoimpl;

import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
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
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.error.exception.ConflictException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StuffDaoImpl extends BaseDaoImpl implements StuffDao {

  public StuffDaoImpl(UniversityRepository universityRepository,
      DepartmentRepository departmentRepository, UserRepository userRepository,
      MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository,
      AuthorityRepository authorityRepository,
      AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository,
      ItemRepository itemRepository, HistoryRepository historyRepository) {
    super(universityRepository, departmentRepository, userRepository, majorRepository,
        majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository,
        stuffRepository, itemRepository, historyRepository);
  }

  @Override
  public List<StuffDto> getAllList() {
    List<StuffDto> output = new ArrayList<>();
    for (StuffEntity stuffEntity : stuffRepository.findAll()) {
      output.add(stuffEntity.toStuffDto());
    }
    return output;
  }

  @Override
  public List<StuffDto> getListByDepartment(@NonNull UUID departmentId) {
    validateDepartmentId(departmentId);

    List<StuffDto> output = new ArrayList<>();
    for (StuffEntity stuffEntity : stuffRepository.findByDepartmentId(departmentId)) {
      output.add(stuffEntity.toStuffDto());
    }
    return output;
  }

  @Override
  public StuffDto getById(@NonNull UUID stuffId) {
    return findStuffEntity(stuffId).toStuffDto();
  }

  @Override
  public StuffDto create(@NonNull UUID stuffId, @NonNull UUID departmentId, @NonNull String name,
      String thumbnail) {
    DepartmentEntity departmentOfNewStuff = getDepartmentEntityOrThrowInvalidIndexException(
        departmentId);

    checkStuffIdConflict(stuffId);
    checkStuffConflict(departmentId, name);

    StuffEntity newStuffEntity = new StuffEntity(stuffId, departmentOfNewStuff, name, thumbnail);
    return stuffRepository.save(newStuffEntity).toStuffDto();
  }

  @Override
  public StuffDto update(@NonNull UUID stuffId, @NonNull UUID newDepartmentId,
      @NonNull String newName, String newThumbnail) {
    StuffEntity target = findStuffEntity(stuffId);
    DepartmentEntity departmentOfNewStuff = getDepartmentEntityOrThrowInvalidIndexException(
        newDepartmentId);

    if (doesIndexChange(target, newDepartmentId, newName)) {
      checkStuffConflict(departmentOfNewStuff.getId(), newName);
    }

    target = target.withDepartment(departmentOfNewStuff).withName(newName)
        .withThumbnail(newThumbnail);
    return stuffRepository.save(target).toStuffDto();
  }

  private boolean doesIndexChange(StuffEntity target, UUID newDepartmentId, String newName) {
    return !(target.getDepartmentId().equals(newDepartmentId) && target.getName().equals(newName));
  }

  private void checkStuffIdConflict(UUID stuffId) {
    if (stuffRepository.existsById(stuffId)) {
      throw new ConflictException();
    }
  }

  private void checkStuffConflict(UUID departmentId, String name) {
    if (stuffRepository.existsByDepartmentIdAndName(departmentId, name)) {
      throw new ConflictException();
    }
  }
}
