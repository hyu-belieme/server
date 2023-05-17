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
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.exception.ItemAmountLimitExceededException;
import com.belieme.apiserver.domain.util.Constants;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends BaseService {

  public ItemService(InitialDataConfig initialData, UniversityDao universityDao,
      DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao,
      StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
    super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao,
        itemDao, historyDao);
  }

  public List<ItemDto> getListByStuff(@NonNull String userToken, @NonNull UUID stuffId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    StuffDto targetStuff = getStuffOrThrowInvalidIndexException(stuffId);
    checkUserPermission(requester, targetStuff.department());
    return getItemListByStuffOrThrowInvalidIndexException(stuffId);
  }

  public ItemDto getById(@NonNull String userToken, @NonNull UUID itemId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    ItemDto item = itemDao.getById(itemId);
    checkUserPermission(requester, item.stuff().department());
    return item;
  }

  public ItemDto create(@NonNull String userToken, @NonNull UUID stuffId) {
    UserDto requester = validateTokenAndGetUser(userToken);
    StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
    checkStaffPermission(requester, stuff.department());

    if (stuff.nextItemNum() > Constants.MAX_ITEM_NUM) {
      throw new ItemAmountLimitExceededException();
    }
    return itemDao.create(UUID.randomUUID(), stuffId, stuff.nextItemNum());
  }

  protected List<ItemDto> getItemListByStuffOrThrowInvalidIndexException(UUID stuffId) {
    try {
      return itemDao.getListByStuff(stuffId);
    } catch (NotFoundException e) {
      throw new InvalidIndexException();
    }
  }
}
