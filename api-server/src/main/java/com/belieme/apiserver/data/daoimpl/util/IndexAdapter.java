package com.belieme.apiserver.data.daoimpl.util;

import com.belieme.apiserver.data.entity.AuthorityEntity;
import com.belieme.apiserver.data.entity.DataEntity;
import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.data.entity.MajorEntity;
import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.data.entity.UniversityEntity;
import com.belieme.apiserver.data.entity.UserEntity;
import com.belieme.apiserver.data.repository.AuthorityRepository;
import com.belieme.apiserver.data.repository.DepartmentRepository;
import com.belieme.apiserver.data.repository.HistoryRepository;
import com.belieme.apiserver.data.repository.ItemRepository;
import com.belieme.apiserver.data.repository.MajorRepository;
import com.belieme.apiserver.data.repository.StuffRepository;
import com.belieme.apiserver.data.repository.UniversityRepository;
import com.belieme.apiserver.data.repository.UserRepository;
import com.belieme.apiserver.error.exception.NotFoundException;
import java.util.UUID;

public class IndexAdapter {

  public static UniversityEntity getUniversityEntity(UniversityRepository universityRepository,
      String name) {
    UniversityEntity target = universityRepository.findByName(name).orElse(null);
    return (UniversityEntity) checkNullAndReturn(target);
  }

  public static DepartmentEntity getDepartmentEntity(DepartmentRepository departmentRepository,
      UUID universityId, String departmentName) {
    DepartmentEntity target = departmentRepository.findByUniversityIdAndName(universityId,
        departmentName).orElse(null);
    return (DepartmentEntity) checkNullAndReturn(target);
  }

  public static UserEntity getUserEntity(UserRepository userRepository, UUID universityId,
      String studentId) {
    UserEntity target = userRepository.findByUniversityIdAndStudentId(universityId, studentId)
        .orElse(null);
    return (UserEntity) checkNullAndReturn(target);
  }

  public static UserEntity getUserEntityByToken(UserRepository userRepository, String token) {
    UserEntity target = userRepository.findByToken(token).orElse(null);
    return (UserEntity) checkNullAndReturn(target);
  }

  public static MajorEntity getMajorEntity(MajorRepository majorRepository, UUID universityId,
      String majorCode) {
    MajorEntity target = majorRepository.findByUniversityIdAndCode(universityId, majorCode)
        .orElse(null);
    return (MajorEntity) checkNullAndReturn(target);
  }

  public static AuthorityEntity getAuthorityEntity(AuthorityRepository authorityRepository,
      UUID departmentId, String permission) {
    AuthorityEntity target = authorityRepository.findByDepartmentIdAndPermission(departmentId,
        permission).orElse(null);
    return (AuthorityEntity) checkNullAndReturn(target);
  }

  public static StuffEntity getStuffEntity(StuffRepository stuffRepository, UUID departmentId,
      String stuffName) {
    StuffEntity target = stuffRepository.findByDepartmentIdAndName(departmentId, stuffName)
        .orElse(null);
    return (StuffEntity) checkNullAndReturn(target);
  }

  public static ItemEntity getItemEntity(ItemRepository itemRepository, UUID stuffId, int num) {
    ItemEntity target = itemRepository.findByStuffIdAndNum(stuffId, num).orElse(null);
    return (ItemEntity) checkNullAndReturn(target);
  }

  public static HistoryEntity getHistoryEntity(HistoryRepository historyRepository, UUID itemId,
      int num) {
    HistoryEntity target = historyRepository.findByItemIdAndNum(itemId, num).orElse(null);
    return (HistoryEntity) checkNullAndReturn(target);
  }

  public static UniversityEntity getUniversityEntity(UniversityRepository universityRepository,
      UUID id) {
    UniversityEntity target = universityRepository.findById(id).orElse(null);
    return (UniversityEntity) checkNullAndReturn(target);
  }

  public static DepartmentEntity getDepartmentEntity(DepartmentRepository departmentRepository,
      UUID id) {
    DepartmentEntity target = departmentRepository.findById(id).orElse(null);
    return (DepartmentEntity) checkNullAndReturn(target);
  }

  public static UserEntity getUserEntity(UserRepository userRepository, UUID id) {
    UserEntity target = userRepository.findById(id).orElse(null);
    return (UserEntity) checkNullAndReturn(target);
  }

  public static MajorEntity getMajorEntity(MajorRepository majorRepository, UUID id) {
    MajorEntity target = majorRepository.findById(id).orElse(null);
    return (MajorEntity) checkNullAndReturn(target);
  }

  public static StuffEntity getStuffEntity(StuffRepository stuffRepository, UUID id) {
    StuffEntity target = stuffRepository.findById(id).orElse(null);
    return (StuffEntity) checkNullAndReturn(target);
  }

  public static ItemEntity getItemEntity(ItemRepository itemRepository, UUID id) {
    ItemEntity target = itemRepository.findById(id).orElse(null);
    return (ItemEntity) checkNullAndReturn(target);
  }

  public static HistoryEntity getHistoryEntity(HistoryRepository historyRepository, UUID id) {
    HistoryEntity target = historyRepository.findById(id).orElse(null);
    return (HistoryEntity) checkNullAndReturn(target);
  }

  private static <T> DataEntity<T> checkNullAndReturn(DataEntity<T> target) {
    if (target == null) {
      throw new NotFoundException();
    }
    return target;
  }
}
