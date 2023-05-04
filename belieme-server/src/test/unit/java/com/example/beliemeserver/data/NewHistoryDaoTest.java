package com.example.beliemeserver.data;

import com.example.beliemeserver.data.daoimpl._new.NewHistoryDaoImpl;
import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewHistoryDaoTest extends BaseDaoTest {
    @InjectMocks
    private NewHistoryDaoImpl historyDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<HistoryDto> execMethod() {
            return historyDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(historyRepository.findAll()).thenReturn(stub.ALL_HISTORIES);
            TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(stub.ALL_HISTORIES));
        }
    }

    @Nested
    @DisplayName("getListByItem()")
    public class TestGetListByItem {
        private NewItemEntity item;
        private UUID itemId;

        private void setItem(NewItemEntity item) {
            this.item = item;
            this.itemId = item.getId();
        }

        protected List<HistoryDto> execMethod() {
            return historyDao.getListByItem(itemId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setItem(randomItem());

            when(historyRepository.findByItemId(itemId)).thenReturn(historiesOnItem(itemId));

            List<NewHistoryEntity> expect = new ArrayList<>();
            for(NewHistoryEntity history : stub.ALL_HISTORIES) {
                if(history.getItemId().equals(itemId)) expect.add(history);
            }
            TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(expect));
        }
    }

    @Nested
    @DisplayName("getListByStuff()")
    public class TestGetListByStuff {
        private NewStuffEntity stuff;
        private UUID stuffId;

        private void setStuff(NewStuffEntity stuff) {
            this.stuff = stuff;
            this.stuffId = stuff.getId();
        }

        protected List<HistoryDto> execMethod() {
            return historyDao.getListByStuff(stuffId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setStuff(randomStuff());

            when(itemRepository.findByStuffId(stuffId)).thenReturn(itemsOnStuff(stuffId));
            for(NewItemEntity item : itemsOnStuff(stuffId)) {
                when(historyRepository.findByItemId(item.getId())).thenReturn(historiesOnItem(item.getId()));
            }

            List<NewHistoryEntity> expect = new ArrayList<>();
            for(NewHistoryEntity history : stub.ALL_HISTORIES) {
                if(history.getItem().getStuffId().equals(stuffId)) expect.add(history);
            }
            TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(expect));
        }
    }

    @Nested
    @DisplayName("getListByDepartment()")
    public class TestGetListByDepartment {
        private NewDepartmentEntity dept;
        private UUID deptId;

        private void setDept(NewDepartmentEntity dept) {
            this.dept = dept;
            this.deptId = dept.getId();
        }

        protected List<HistoryDto> execMethod() {
            return historyDao.getListByDepartment(deptId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setDept(randomDept());

            when(stuffRepository.findByDepartmentId(deptId)).thenReturn(stuffsOnDept(deptId));
            for(NewStuffEntity stuff : stuffsOnDept(deptId)) {
                when(itemRepository.findByStuffId(stuff.getId())).thenReturn(itemsOnStuff(stuff.getId()));
                for(NewItemEntity item : itemsOnStuff(stuff.getId())) {
                    when(historyRepository.findByItemId(item.getId())).thenReturn(historiesOnItem(item.getId()));
                }
            }

            List<NewHistoryEntity> expect = new ArrayList<>();
            for(NewHistoryEntity history : stub.ALL_HISTORIES) {
                if(history.getItem().getStuff().getDepartmentId().equals(deptId)) expect.add(history);
            }
            TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(expect));
        }
    }

    @Nested
    @DisplayName("getListByDepartmentAndRequester()")
    public class TestGetListByDepartmentAndRequester {
        private NewUserEntity requester;
        private UUID requesterId;

        private NewDepartmentEntity dept;
        private UUID deptId;

        private void setUser(NewUserEntity requester) {
            this.requester = requester;
            this.requesterId = requester.getId();
        }

        private void setDept(NewDepartmentEntity dept) {
            this.dept = dept;
            this.deptId = dept.getId();
        }

        protected List<HistoryDto> execMethod() {
            return historyDao.getListByDepartmentAndRequester(deptId, requesterId);
        }

        @RepeatedTest(20)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setDept(randomDept());
            setUser(randomUser());

            when(historyRepository.findByRequesterId(requesterId))
                    .thenReturn(historiesByRequesters(requesterId));

            List<NewHistoryEntity> expect = new ArrayList<>();
            for(NewHistoryEntity history : stub.ALL_HISTORIES) {
                if(history.getItem().getStuff().getDepartmentId().equals(deptId)
                        && history.getRequesterId() != null && history.getRequesterId().equals(requesterId)) {
                    expect.add(history);
                }
            }
            TestHelper.listCompareTest(this::execMethod, toHistoryDtoList(expect));
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById {
        private NewHistoryEntity history;
        private UUID historyId;

        private void setHistory(NewHistoryEntity history) {
            this.history = history;
            this.historyId = history.getId();
        }

        protected HistoryDto execMethod() {
            return historyDao.getById(historyId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setHistory(randomHistory());

            when(historyRepository.findById(historyId)).thenReturn(Optional.of(history));
            TestHelper.objectCompareTest(this::execMethod, history.toHistoryDto());
        }

        @Test
        @DisplayName("[ERROR]_[`id`에 해당하는 `history`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setHistory(randomHistory());

            when(historyRepository.findById(historyId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private NewHistoryEntity history;

        private void setHistory(NewHistoryEntity history) {
            this.history = history;
        }

        protected HistoryDto execMethod() {
            return historyDao.create(
                    history.getId(), history.getItemId(), history.getNum(),
                    history.getRequesterId(), history.getApproveManagerId(),
                    history.getReturnManagerId(), history.getLostManagerId(),
                    history.getCancelManagerId(), history.getRequestedAt(),
                    history.getApprovedAt(), history.getReturnedAt(),
                    history.getLostAt(), history.getCanceledAt()
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setHistory(randomHistory());

            when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
            when(historyRepository.existsById(history.getId())).thenReturn(false);
            when(historyRepository.existsByItemIdAndNum(history.getItemId(), history.getNum())).thenReturn(false);
            if(history.getRequester() != null) when(userRepository.findById(history.getRequesterId())).thenReturn(Optional.of(history.getRequester()));
            if(history.getApproveManager() != null) when(userRepository.findById(history.getApproveManagerId())).thenReturn(Optional.of(history.getApproveManager()));
            if(history.getReturnManager() != null) when(userRepository.findById(history.getReturnManagerId())).thenReturn(Optional.of(history.getReturnManager()));
            if(history.getLostManager() != null) when(userRepository.findById(history.getLostManagerId())).thenReturn(Optional.of(history.getLostManager()));
            if(history.getCancelManager() != null) when(userRepository.findById(history.getCancelManagerId())).thenReturn(Optional.of(history.getCancelManager()));

            when(historyRepository.save(mockArg(history))).thenReturn(history);

            TestHelper.objectCompareTest(this::execMethod, history.toHistoryDto());

            verify(historyRepository).save(mockArg(history));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`itemId`에 해당하는 `item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_universityNotFound_InvalidIndexException() {
            setHistory(randomHistory());

            when(itemRepository.findById(history.getItemId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `id`를 갖는 `history`가 존재할 시]_[ConflictException]")
        public void ERROR_IdConflict_ConflictException() {
            setHistory(randomHistory());

            when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
            when(historyRepository.existsById(history.getId())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `history`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setHistory(randomHistory());

            when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
            when(historyRepository.existsById(history.getId())).thenReturn(false);
            when(historyRepository.existsByItemIdAndNum(history.getItemId(), history.getNum())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`userId`에 해당하는 `user`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_wrongUnivId_InvalidIndexException() {
            setHistory(randomHistory());

            when(itemRepository.findById(history.getItemId())).thenReturn(Optional.of(history.getItem()));
            when(historyRepository.existsByItemIdAndNum(history.getItemId(), history.getNum())).thenReturn(false);
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        private NewHistoryEntity target;
        private UUID targetId;
        private NewHistoryEntity newHistory;

        private void setTarget(NewHistoryEntity target) {
            this.target = target;
            this.targetId = target.getId();
        }

        private NewHistoryEntity updatedHistory() {
            return target
                    .withItem(newHistory.getItem())
                    .withNum(newHistory.getNum())
                    .withRequester(newHistory.getRequester())
                    .withApproveManager(newHistory.getApproveManager())
                    .withReturnManager(newHistory.getReturnManager())
                    .withLostManager(newHistory.getLostManager())
                    .withCancelManager(newHistory.getCancelManager())
                    .withRequestedAt(newHistory.getRequestedAt())
                    .withApprovedAt(newHistory.getApprovedAt())
                    .withReturnedAt(newHistory.getReturnedAt())
                    .withLostAt(newHistory.getLostAt())
                    .withCanceledAt(newHistory.getCanceledAt());
        }

        protected HistoryDto execMethod() {
            return historyDao.update(
                    targetId, newHistory.getItemId(), newHistory.getNum(),
                    newHistory.getRequesterId(), newHistory.getApproveManagerId(),
                    newHistory.getReturnManagerId(), newHistory.getLostManagerId(),
                    newHistory.getCancelManagerId(), newHistory.getRequestedAt(),
                    newHistory.getApprovedAt(), newHistory.getReturnedAt(),
                    newHistory.getLostAt(), newHistory.getCanceledAt()
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`index`를 변경하지 않을 시]_[-]")
        public void SUCCESS_notChangeIndex() {
            setTarget(randomHistory());
            newHistory = target
                    .withApproveManager(randomUser())
                    .withApprovedAt(1683076516);

            when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(itemRepository.findById(newHistory.getItemId())).thenReturn(Optional.of(newHistory.getItem()));

            if(newHistory.getRequester() != null) when(userRepository.findById(newHistory.getRequesterId())).thenReturn(Optional.of(newHistory.getRequester()));
            if(newHistory.getApproveManager() != null) when(userRepository.findById(newHistory.getApproveManagerId())).thenReturn(Optional.of(newHistory.getApproveManager()));
            if(newHistory.getReturnManager() != null) when(userRepository.findById(newHistory.getReturnManagerId())).thenReturn(Optional.of(newHistory.getReturnManager()));
            if(newHistory.getLostManager() != null) when(userRepository.findById(newHistory.getLostManagerId())).thenReturn(Optional.of(newHistory.getLostManager()));
            if(newHistory.getCancelManager() != null) when(userRepository.findById(newHistory.getCancelManagerId())).thenReturn(Optional.of(newHistory.getCancelManager()));

            when(historyRepository.save(mockArg(updatedHistory()))).thenReturn(updatedHistory());

            TestHelper.objectCompareTest(this::execMethod, updatedHistory().toHistoryDto());

            verify(historyRepository).save(mockArg(updatedHistory()));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`index`를 변경할 시]_[-]")
        public void SUCCESS_changeIndex() {
            setTarget(randomHistory());
            newHistory = randomHistory()
                    .withItem(randomItem())
                    .withNum(9000)
                    .withReturnManager(randomUser())
                    .withReturnedAt(1683076516);

            when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(itemRepository.findById(newHistory.getItemId())).thenReturn(Optional.of(newHistory.getItem()));
            when(historyRepository.existsByItemIdAndNum(newHistory.getItemId(), newHistory.getNum())).thenReturn(false);

            if(newHistory.getRequester() != null) when(userRepository.findById(newHistory.getRequesterId())).thenReturn(Optional.of(newHistory.getRequester()));
            if(newHistory.getApproveManager() != null) when(userRepository.findById(newHistory.getApproveManagerId())).thenReturn(Optional.of(newHistory.getApproveManager()));
            if(newHistory.getReturnManager() != null) when(userRepository.findById(newHistory.getReturnManagerId())).thenReturn(Optional.of(newHistory.getReturnManager()));
            if(newHistory.getLostManager() != null) when(userRepository.findById(newHistory.getLostManagerId())).thenReturn(Optional.of(newHistory.getLostManager()));
            if(newHistory.getCancelManager() != null) when(userRepository.findById(newHistory.getCancelManagerId())).thenReturn(Optional.of(newHistory.getCancelManager()));

            when(historyRepository.save(mockArg(updatedHistory()))).thenReturn(updatedHistory());

            TestHelper.objectCompareTest(this::execMethod, updatedHistory().toHistoryDto());

            verify(historyRepository).save(mockArg(updatedHistory()));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`id`에 해당하는 `history`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_majorNotFound_NotFoundException() {
            setTarget(randomHistory());
            newHistory = randomHistory()
                    .withItem(randomItem())
                    .withNum(9000)
                    .withReturnManager(randomUser())
                    .withReturnedAt(1683076516);

            when(historyRepository.findById(targetId)).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }


        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`itemId`에 해당하는 `item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemNotFound_InvalidIndexException() {
            setTarget(randomHistory());
            newHistory = randomHistory()
                    .withItem(randomItem())
                    .withNum(9000)
                    .withReturnManager(randomUser())
                    .withReturnedAt(1683076516);

            when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(itemRepository.findById(newHistory.getItemId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`userId`에 해당하는 `user`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_wrongUnivId_InvalidIndexException() {
            setTarget(randomHistory());
            newHistory = randomHistory()
                    .withItem(randomItem())
                    .withNum(9000)
                    .withReturnManager(randomUser())
                    .withReturnedAt(1683076516);

            when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(itemRepository.findById(newHistory.getItemId())).thenReturn(Optional.of(newHistory.getItem()));
            when(historyRepository.existsByItemIdAndNum(newHistory.getItemId(), newHistory.getNum())).thenReturn(false);
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `history`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setTarget(randomHistory());
            newHistory = randomHistory()
                    .withItem(randomItem())
                    .withNum(9000)
                    .withReturnManager(randomUser())
                    .withReturnedAt(1683076516);

            when(historyRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(itemRepository.findById(newHistory.getItemId())).thenReturn(Optional.of(newHistory.getItem()));
            when(historyRepository.existsByItemIdAndNum(newHistory.getItemId(), newHistory.getNum())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    private List<NewHistoryEntity> historiesByRequesters(UUID requesterId) {
        return stub.ALL_HISTORIES.stream().filter((history) -> history.getRequesterId() != null && history.getRequesterId().equals(requesterId)).toList();
    }

    private List<NewStuffEntity> stuffsOnDept(UUID deptId) {
        return stub.ALL_STUFFS.stream().filter((stuff) -> stuff.getDepartmentId().equals(deptId)).toList();
    }

    private List<NewItemEntity> itemsOnStuff(UUID stuffId) {
        return stub.ALL_ITEMS.stream().filter((item) -> item.getStuffId().equals(stuffId)).toList();
    }

    private List<NewHistoryEntity> historiesOnItem(UUID itemId) {
        return stub.ALL_HISTORIES.stream().filter((history) -> history.getItemId().equals(itemId)).toList();
    }
}