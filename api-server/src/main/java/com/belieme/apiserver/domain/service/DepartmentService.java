package com.belieme.apiserver.domain.service;

import com.belieme.apiserver.config.initdata.InitialDataConfig;
import com.belieme.apiserver.config.initdata.container.DepartmentInfo;
import com.belieme.apiserver.config.initdata.container.MajorInfo;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dao.DepartmentDao;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dao.MajorDao;
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dao.UserDao;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.MajorDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.error.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends BaseService {

  public DepartmentService(InitialDataConfig initialData, UniversityDao universityDao,
      DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao,
      StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
    super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao,
        itemDao, historyDao);
  }

  public void initializeDepartments() {
    for (DepartmentInfo department : initialData.departmentInfos().values()) {
      if (departmentDao.checkExistById(department.id())) {
        departmentDao.update(
            department.id(),
            department.universityId(),
            department.name(),
            department.baseMajors().stream().map(MajorInfo::id).toList()
        );
        createOrUpdateAuthorities(department.id());
        continue;
      }
      departmentDao.create(
          department.id(),
          department.universityId(),
          department.name(),
          department.baseMajors().stream().map(MajorInfo::id).toList()
      );
      createOrUpdateAuthorities(department.id());
    }
  }

  public List<DepartmentDto> getAccessibleList(@NonNull String userToken) {
    List<DepartmentDto> output = new ArrayList<>();

    UserDto requester = validateTokenAndGetUser(userToken);
    List<DepartmentDto> allDepartment = departmentDao.getAllList();
    for (DepartmentDto department : allDepartment) {
      if (requester.getMaxPermission(department).hasUserPermission()) {
        output.add(department);
      }
    }

    return output;
  }

  public DepartmentDto getById(
      @NonNull String userToken, @NonNull UUID departmentId
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);

    return departmentDao.getById(departmentId);
  }

  public DepartmentDto create(
      @NonNull String userToken, @NonNull UUID universityId,
      @NonNull String departmentName, @NonNull List<String> majorCodes
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);

    List<UUID> baseMajorIds = new ArrayList<>();
    for (String majorCode : majorCodes) {
      baseMajorIds.add(getMajorOrCreate(universityId, majorCode).id());
    }

    DepartmentDto newDepartment = departmentDao.create(
        UUID.randomUUID(),
        universityId,
        departmentName,
        baseMajorIds
    );
    createOrUpdateAuthorities(newDepartment.id());
    return newDepartment;
  }

  public DepartmentDto update(
      @NonNull String userToken, @NonNull UUID departmentId,
      String newDepartmentName, List<String> newMajorCodes
  ) {
    UserDto requester = validateTokenAndGetUser(userToken);
    checkDeveloperPermission(requester);

    DepartmentDto oldDepartment = departmentDao.getById(departmentId);

      if (newDepartmentName == null && newMajorCodes == null) {
          return oldDepartment;
      }
      if (newDepartmentName == null) {
          newDepartmentName = oldDepartment.name();
      }

    List<MajorDto> newBaseMajors = oldDepartment.baseMajors();
    if (newMajorCodes != null) {
      newBaseMajors = new ArrayList<>();
      for (String majorCode : newMajorCodes) {
        newBaseMajors.add(getMajorOrCreate(oldDepartment.university().id(), majorCode));
      }
    }

    DepartmentDto newDepartment = new DepartmentDto(departmentId, oldDepartment.university(),
        newDepartmentName, newBaseMajors);
    return departmentDao.update(
        departmentId,
        oldDepartment.university().id(),
        newDepartmentName,
        newBaseMajors.stream().map(MajorDto::id).toList()
    );
  }

  private MajorDto getMajorOrCreate(UUID universityId, String majorCode) {
    try {
      return majorDao.getByIndex(universityId, majorCode);
    } catch (NotFoundException e) {
      return majorDao.create(UUID.randomUUID(), universityId, majorCode);
    }
  }

  private void createOrUpdateAuthorities(UUID departmentId) {
    for (Permission permission : Permission.values()) {
        if (authorityDao.checkExistByIndex(departmentId, permission)) {
            continue;
        }
      authorityDao.create(departmentId, permission);
    }
  }
}
