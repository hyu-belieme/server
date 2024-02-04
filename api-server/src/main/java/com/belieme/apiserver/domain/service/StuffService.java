package com.belieme.apiserver.domain.service;

import com.belieme.apiserver.config.initdata.InitialDataConfig;
import com.belieme.apiserver.domain.dao.AuthorityDao;
import com.belieme.apiserver.domain.dao.DepartmentDao;
import com.belieme.apiserver.domain.dao.HistoryDao;
import com.belieme.apiserver.domain.dao.ItemDao;
import com.belieme.apiserver.domain.dao.MajorDao;
import com.belieme.apiserver.domain.dao.StuffDao;
import com.belieme.apiserver.domain.dao.UniversityDao;
import com.belieme.apiserver.domain.dao.UserDao;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.exception.ItemAmountLimitExceededException;
import com.belieme.apiserver.domain.util.Constants;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class StuffService extends BaseService {

  public StuffService(InitialDataConfig initialData, UniversityDao universityDao,
      DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao,
      StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
    super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao,
        itemDao, historyDao);
  }

  public List<StuffDto> getListByDepartment(@NonNull String userToken, @NonNull UUID departmentId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
    checkUserPermission(requester, department);

    return stuffDao.getListByDepartment(departmentId);
  }

  public StuffDto getById(@NonNull String userToken, @NonNull UUID stuffId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    StuffDto stuff = stuffDao.getById(stuffId);
    checkUserPermission(requester, stuff.department());

    return stuff;
  }

  public StuffDto create(@NonNull String userToken, @NonNull UUID departmentId,
      @NonNull String name, @NonNull String thumbnail, @NonNull String desc, Integer amount) {
    UserDto requester = validateTokenAndGetUser(userToken);
    System.out.println(departmentId);
    DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
    checkStaffPermission(requester, department);

    StuffDto newStuff = stuffDao.create(UUID.randomUUID(), departmentId, name, thumbnail, desc);

    if (amount == null) {
      return newStuff;
    }
    if (amount > Constants.MAX_ITEM_NUM) {
      throw new ItemAmountLimitExceededException();
    }

    for (int i = 0; i < amount; i++) {
      ItemDto newItem = itemDao.create(UUID.randomUUID(), newStuff.id(), i + 1);
      newStuff = newStuff.withItemAdd(newItem);
    }
    return newStuff;
  }

  public StuffDto update(@NonNull String userToken, @NonNull UUID stuffId, String newName,
      String newThumbnail, String newDesc) {
    UserDto requester = validateTokenAndGetUser(userToken);
    StuffDto oldStuff = stuffDao.getById(stuffId);
    checkStaffPermission(requester, oldStuff.department());

    if (newName == null && newThumbnail == null && newDesc == null) {
      return oldStuff;
    }
    if (newName == null) {
      newName = oldStuff.name();
    }
    if (newThumbnail == null) {
      newThumbnail = oldStuff.thumbnail();
    }
    if (newDesc == null) {
      newDesc = oldStuff.desc();
    }

    return stuffDao.update(stuffId, oldStuff.department().id(), newName, newThumbnail, newDesc);
  }
}
