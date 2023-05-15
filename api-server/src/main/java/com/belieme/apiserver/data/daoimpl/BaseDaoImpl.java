package com.belieme.apiserver.data.daoimpl;

import com.belieme.apiserver.data.daoimpl.util.IndexAdapter;
import com.belieme.apiserver.data.entity.AuthorityEntity;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.entity.MajorEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.data.entity.UniversityEntity;
import com.belieme.apiserver.data.entity.UserEntity;
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
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseDaoImpl {

  protected final UniversityRepository universityRepository;
  protected final DepartmentRepository departmentRepository;
  protected final UserRepository userRepository;
  protected final MajorRepository majorRepository;
  protected final MajorDepartmentJoinRepository majorDepartmentJoinRepository;
  protected final AuthorityRepository authorityRepository;
  protected final AuthorityUserJoinRepository authorityUserJoinRepository;
  protected final StuffRepository stuffRepository;
  protected final ItemRepository itemRepository;
  protected final HistoryRepository historyRepository;

  @Autowired
  public BaseDaoImpl(UniversityRepository universityRepository,
      DepartmentRepository departmentRepository, UserRepository userRepository,
      MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository,
      AuthorityRepository authorityRepository,
      AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository,
      ItemRepository itemRepository, HistoryRepository historyRepository) {
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

  protected void validateUniversityId(UUID universityId) {
      if (!universityRepository.existsById(universityId)) {
          throw new InvalidIndexException();
      }
  }

  protected void validateDepartmentId(UUID departmentId) {
      if (!departmentRepository.existsById(departmentId)) {
          throw new InvalidIndexException();
      }
  }

  protected void validateUserId(UUID userId) {
      if (!userRepository.existsById(userId)) {
          throw new InvalidIndexException();
      }
  }

  protected void validateStuffId(UUID stuffId) {
      if (!stuffRepository.existsById(stuffId)) {
          throw new InvalidIndexException();
      }
  }

  protected void validateItemId(UUID itemId) {
      if (!itemRepository.existsById(itemId)) {
          throw new InvalidIndexException();
      }
  }

  protected UniversityEntity findUniversityEntity(UUID id) {
    return IndexAdapter.getUniversityEntity(universityRepository, id);
  }

  protected UniversityEntity findUniversityEntity(String universityName) {
    return IndexAdapter.getUniversityEntity(universityRepository, universityName);
  }

  protected DepartmentEntity findDepartmentEntity(UUID id) {
    return IndexAdapter.getDepartmentEntity(departmentRepository, id);
  }

  protected DepartmentEntity findDepartmentEntity(String universityName, String departmentName) {
    UUID universityId = findUniversityEntity(universityName).getId();

    return IndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentName);
  }

  protected UserEntity findUserEntity(UUID id) {
    return IndexAdapter.getUserEntity(userRepository, id);
  }

  protected UserEntity findUserEntity(UUID universityId, String studentId) {
    return IndexAdapter.getUserEntity(userRepository, universityId, studentId);
  }

  protected UserEntity findUserEntity(String universityName, String studentId) {
    UUID universityId = findUniversityEntity(universityName).getId();

    return IndexAdapter.getUserEntity(userRepository, universityId, studentId);
  }

  protected UserEntity findUserEntityByToken(String token) {
    return IndexAdapter.getUserEntityByToken(userRepository, token);
  }

  protected MajorEntity findMajorEntity(UUID id) {
    return IndexAdapter.getMajorEntity(majorRepository, id);
  }

  protected MajorEntity findMajorEntity(UUID universityId, String majorCode) {
    return IndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
  }

  protected AuthorityEntity findAuthorityEntity(UUID departmentId, String permission) {
    return IndexAdapter.getAuthorityEntity(authorityRepository, departmentId, permission);
  }

  protected AuthorityEntity findAuthorityEntity(String universityName, String departmentName,
      String permission) {
    UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();

    return IndexAdapter.getAuthorityEntity(authorityRepository, departmentId, permission);
  }

  protected StuffEntity findStuffEntity(UUID stuffId) {
    return IndexAdapter.getStuffEntity(stuffRepository, stuffId);
  }

  protected StuffEntity findStuffEntity(String universityName, String departmentName,
      String stuffName) {
    UUID departmentId = findDepartmentEntity(universityName, departmentName).getId();

    return IndexAdapter.getStuffEntity(stuffRepository, departmentId, stuffName);
  }

  protected ItemEntity findItemEntity(UUID itemId) {
    return IndexAdapter.getItemEntity(itemRepository, itemId);
  }

  protected ItemEntity findItemEntity(String universityName, String departmentName,
      String stuffName, int itemNum) {
    UUID stuffId = findStuffEntity(universityName, departmentName, stuffName).getId();

    return IndexAdapter.getItemEntity(itemRepository, stuffId, itemNum);
  }

  protected HistoryEntity findHistoryEntity(UUID historyId) {
    return IndexAdapter.getHistoryEntity(historyRepository, historyId);
  }

  protected HistoryEntity findHistoryEntity(String universityName, String departmentName,
      String stuffName, int itemNum, int historyNum) {
    UUID itemId = findItemEntity(universityName, departmentName, stuffName, itemNum).getId();

    return IndexAdapter.getHistoryEntity(historyRepository, itemId, historyNum);
  }

  protected UniversityEntity getUniversityEntityOrThrowInvalidIndexException(UUID universityId) {
    try {
      return findUniversityEntity(universityId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }

  protected DepartmentEntity getDepartmentEntityOrThrowInvalidIndexException(UUID departmentId) {
    try {
      return findDepartmentEntity(departmentId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }

  protected UserEntity getUserEntityOrThrowInvalidIndexException(UUID userId) {
    try {
      return findUserEntity(userId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }

  protected StuffEntity getStuffEntityOrThrowInvalidIndexException(UUID stuffId) {
    try {
      return findStuffEntity(stuffId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }

  protected ItemEntity getItemEntityOrThrowInvalidIndexException(UUID itemId) {
    try {
      return findItemEntity(itemId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }

  protected HistoryEntity getHistoryEntityOrThrowInvalidIndexException(UUID historyId) {
    try {
      return findHistoryEntity(historyId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }
}
