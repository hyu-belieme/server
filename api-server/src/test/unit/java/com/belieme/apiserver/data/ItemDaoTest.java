package com.belieme.apiserver.data;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.belieme.apiserver.data.daoimpl.ItemDaoImpl;
import com.belieme.apiserver.data.entity.ItemEntity;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.error.exception.ConflictException;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.TestHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemDaoTest extends BaseDaoTest {

  @InjectMocks
  private ItemDaoImpl itemDao;

  @Nested
  @DisplayName("getAllList()")
  public class TestGetAllList {

    protected List<ItemDto> execMethod() {
      return itemDao.getAllList();
    }

    @Test()
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      when(itemRepository.findAll()).thenReturn(stub.ALL_ITEMS);
      TestHelper.listCompareTest(this::execMethod, toItemDtoList(stub.ALL_ITEMS));
    }
  }

  @Nested
  @DisplayName("getListByStuff()")
  public class TestGetListByStuff {

    private final UUID stuffId = randomStuff().getId();

    protected List<ItemDto> execMethod() {
      return itemDao.getListByStuff(stuffId);
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      List<ItemEntity> targets = new ArrayList<>();
      for (ItemEntity item : stub.ALL_ITEMS) {
        if (item.getStuffId().equals(stuffId)) {
          targets.add(item);
        }
      }

      when(stuffRepository.existsById(stuffId)).thenReturn(true);
      when(itemRepository.findByStuffId(stuffId)).thenReturn(targets);
      TestHelper.listCompareTest(this::execMethod, toItemDtoList(targets));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[잘못된 `stuffId`가 주어졌을 시]_[InvalidIndexException]")
    public void ERROR_wrongStuffId_invalidIndexException() {
      when(stuffRepository.existsById(stuffId)).thenReturn(false);

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }
  }

  @Nested
  @DisplayName("getById()")
  public class TestGetById {

    private ItemEntity item;
    private UUID itemId;

    private void setItem(ItemEntity item) {
      this.item = item;
      this.itemId = item.getId();
    }

    protected ItemDto execMethod() {
      return itemDao.getById(itemId);
    }

    @Test
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setItem(randomItem());

      when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
      TestHelper.objectCompareTest(this::execMethod, item.toItemDto());
    }

    @Test
    @DisplayName("[ERROR]_[`id`에 해당하는 `item`이 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_notFound_NotFoundException() {
      setItem(randomItem());

      when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }
  }

  @Nested
  @DisplayName("create()")
  public class TestCreate {

    private ItemEntity item;

    private void setItem(ItemEntity item) {
      this.item = item.withLastHistory(null);
    }

    protected ItemDto execMethod() {
      return itemDao.create(item.getId(), item.getStuffId(), item.getNum());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[-]_[-]")
    public void SUCCESS() {
      setItem(randomItem());

      when(stuffRepository.findById(item.getStuffId())).thenReturn(Optional.of(item.getStuff()));
      when(itemRepository.existsById(item.getId())).thenReturn(false);
      when(itemRepository.existsByStuffIdAndNum(item.getStuffId(), item.getNum())).thenReturn(
          false);
      when(itemRepository.save(mockArg(item))).thenReturn(item);

      TestHelper.objectCompareTest(this::execMethod, item.toItemDto());

      verify(itemRepository).save(mockArg(item));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`stuffId`에 해당하는 `stuff`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_universityNotFound_InvalidIndexException() {
      setItem(randomItem());

      when(stuffRepository.findById(item.getStuffId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `id`를 갖는 `item`이 존재할 시]_[ConflictException]")
    public void ERROR_idConflict_ConflictException() {
      setItem(randomItem());

      when(stuffRepository.findById(item.getStuffId())).thenReturn(Optional.of(item.getStuff()));
      when(itemRepository.existsById(item.getId())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `item`이 존재할 시]_[ConflictException]")
    public void ERROR_IndexConflict_ConflictException() {
      setItem(randomItem());

      when(stuffRepository.findById(item.getStuffId())).thenReturn(Optional.of(item.getStuff()));
      when(itemRepository.existsById(item.getId())).thenReturn(false);
      when(itemRepository.existsByStuffIdAndNum(item.getStuffId(), item.getNum())).thenReturn(true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }
  }

  @Nested
  @DisplayName("update()")
  public class TestUpdate {

    private ItemEntity target;
    private UUID targetId;
    private ItemEntity newItem;

    private void setTarget(ItemEntity target) {
      this.target = target;
      this.targetId = target.getId();
    }

    private ItemEntity updatedItem() {
      return target.withStuff(newItem.getStuff()).withNum(newItem.getNum())
          .withLastHistory(newItem.getLastHistory());
    }

    protected ItemDto execMethod() {
      return itemDao.update(targetId, newItem.getStuffId(), newItem.getNum(),
          newItem.getLastHistoryId());
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`index`를 변경하지 않을 시]_[-]")
    public void SUCCESS_notChangeIndex() {
      setTarget(randomItem());
      newItem = target.withLastHistory(randomHistory());

      when(itemRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(stuffRepository.findById(newItem.getStuffId())).thenReturn(
          Optional.of(newItem.getStuff()));
      if (newItem.getLastHistory() != null) {
        when(historyRepository.findById(newItem.getLastHistoryId())).thenReturn(
            Optional.of(newItem.getLastHistory()));
      }
      when(itemRepository.save(mockArg(updatedItem()))).thenReturn(updatedItem());

      TestHelper.objectCompareTest(this::execMethod, updatedItem().toItemDto());

      verify(itemRepository).save(mockArg(updatedItem()));
    }

    @RepeatedTest(10)
    @DisplayName("[SUCCESS]_[`index`를 변경할 시]_[-]")
    public void SUCCESS_changeIndex() {
      setTarget(randomItem());
      newItem = randomItem().withStuff(randomStuff()).withNum(999);

      when(itemRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(stuffRepository.findById(newItem.getStuffId())).thenReturn(
          Optional.of(newItem.getStuff()));
      if (newItem.getLastHistory() != null) {
        when(historyRepository.findById(newItem.getLastHistoryId())).thenReturn(
            Optional.of(newItem.getLastHistory()));
      }
      when(itemRepository.existsByStuffIdAndNum(newItem.getStuffId(), newItem.getNum())).thenReturn(
          false);
      when(itemRepository.save(mockArg(updatedItem()))).thenReturn(updatedItem());

      TestHelper.objectCompareTest(this::execMethod, updatedItem().toItemDto());

      verify(itemRepository).save(mockArg(updatedItem()));
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`id`에 해당하는 `item`가 존재하지 않을 시]_[NotFoundException]")
    public void ERROR_majorNotFound_NotFoundException() {
      setTarget(randomItem());
      newItem = randomItem().withStuff(randomStuff()).withNum(2);

      when(itemRepository.findById(targetId)).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
    }


    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`stuffId`에 해당하는 `stuff`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_stuffNotFound_InvalidIndexException() {
      setTarget(randomItem());
      newItem = randomItem().withStuff(randomStuff()).withNum(2);

      when(itemRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(stuffRepository.findById(newItem.getStuffId())).thenReturn(Optional.empty());

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[`lastHistoryId`에 해당하는 `history`가 존재하지 않을 시]_[InvalidIndexException]")
    public void ERROR_wrongLastHistoryId_InvalidIndexException() {
      setTarget(randomItem());
      newItem = randomItem().withStuff(randomStuff()).withNum(2).withLastHistory(randomHistory());

      when(itemRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(stuffRepository.findById(newItem.getStuffId())).thenReturn(
          Optional.of(newItem.getStuff()));
      if (newItem.getLastHistory() != null) {
        when(historyRepository.findById(newItem.getLastHistoryId())).thenReturn(Optional.empty());
      }

      TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
    }

    @RepeatedTest(10)
    @DisplayName("[ERROR]_[동일한 `index`를 갖는 `stuff`가 존재할 시]_[ConflictException]")
    public void ERROR_IndexConflict_ConflictException() {
      setTarget(randomItem());
      newItem = randomItem().withStuff(randomStuff()).withNum(999);

      when(itemRepository.findById(targetId)).thenReturn(Optional.of(target));
      when(stuffRepository.findById(newItem.getStuffId())).thenReturn(
          Optional.of(newItem.getStuff()));
      if (newItem.getLastHistory() != null) {
        when(historyRepository.findById(newItem.getLastHistoryId())).thenReturn(
            Optional.of(newItem.getLastHistory()));
      }
      when(itemRepository.existsByStuffIdAndNum(newItem.getStuffId(), newItem.getNum())).thenReturn(
          true);

      TestHelper.exceptionTest(this::execMethod, ConflictException.class);
    }
  }
}
