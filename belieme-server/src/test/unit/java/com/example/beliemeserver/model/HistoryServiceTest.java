package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.service.HistoryService;
import com.example.beliemeserver.util.RandomFilter;
import com.example.beliemeserver.util.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest extends BaseServiceTest {
    private final DepartmentDto TEST_DEPT = stub.HYU_CSE_DEPT;

    @InjectMocks
    private HistoryService historyService;

    @Nested
    @DisplayName("getListByDepartment")
    public final class TestGetListByDepartment extends HistoryNestedTest {
        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.STAFF));

            historyList = getHistoryListByDept(dept);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByDepartment(userToken, univCode, deptCode);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartment(univCode, deptCode))
                    .thenReturn(historyList);

            System.out.println("Expected : " + historyList);
            TestHelper.listCompareTest(
                    this::execMethod,
                    historyList
            );
        }

        private List<HistoryDto> getHistoryListByDept(DepartmentDto dept) {
            return stub.ALL_HISTORIES.stream()
                    .filter((history) -> dept.matchUniqueKey(history.item().stuff().department()))
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("getListByStuff()")
    public final class TestGetListByStuff extends HistoryNestedTest {
        private StuffDto stuff;
        private String stuffName;

        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.STAFF));
            setStuff(randomStuffByDept(dept));
            historyList = getHistoryListByStuff(stuff);
        }

        private void setStuff(StuffDto stuff) {
            this.stuff = stuff;
            stuffName = stuff.name();
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByStuff(
                    userToken, univCode, deptCode, stuffName);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByStuff(univCode, deptCode, stuffName))
                    .thenReturn(historyList);

            System.out.println("Expected : " + historyList);
            TestHelper.listCompareTest(
                    this::execMethod,
                    historyList
            );
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByStuff(univCode, deptCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByStuff(StuffDto stuff) {
            return stub.ALL_HISTORIES.stream()
                    .filter((history) -> stuff.matchUniqueKey(history.item().stuff()))
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("getListByItem()")
    public final class TestGetListByItem extends HistoryNestedTest {
        private ItemDto item;
        private String stuffName;
        private int itemNum;

        private List<HistoryDto> historyList;

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuffName = item.stuff().name();
            this.itemNum = item.num();
        }

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.STAFF));
            setItem(randomItemByDept(dept));

            historyList = getHistoryListByItem(item);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserByDeptAndAuth(dept, AuthorityDto.Permission.USER));
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByItem(userToken,
                    univCode, deptCode, stuffName, itemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByItem(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(historyList);

            System.out.println("Expected : " + historyList);
            TestHelper.listCompareTest(
                    this::execMethod,
                    historyList
            );
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 `index`의 `item`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByItem(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByItem(ItemDto item) {
            return stub.ALL_HISTORIES.stream()
                    .filter((history) -> item.matchUniqueKey(history.item()))
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("getListByDepartmentAndRequester()")
    public final class TestGetListByDepartmentAndRequester extends HistoryNestedTest {
        private UserDto historyRequester;
        private String historyRequesterUnivCode;
        private String historyRequesterStudentId;

        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));

            setHistoryRequester(requester);
            historyList = getHistoryListByDeptAndRequester(dept, historyRequester);
        }

        private void setHistoryRequester(UserDto historyRequester) {
            this.historyRequester = historyRequester;
            this.historyRequesterUnivCode = historyRequester.university().code();
            this.historyRequesterStudentId = historyRequester.studentId();
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByDepartmentAndRequester(
                    userToken, univCode, deptCode,
                    historyRequesterUnivCode,
                    historyRequesterStudentId
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[본인의 `History List`에 대한 `request`일 시]_[-]")
        public void SUCCESS_getHistoryListHerSelf() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(userDao.getByIndex(historyRequesterUnivCode, historyRequesterStudentId))
                    .thenReturn(historyRequester);
            when(historyDao.getListByDepartmentAndRequester(
                    univCode, deptCode,
                    historyRequesterUnivCode,
                    historyRequesterStudentId)
            ).thenReturn(historyList);

            TestHelper.listCompareTest(this::execMethod, historyList);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[타인의 `History List`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
        public void SUCCESS_getHistoryListOfOthers() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.STAFF));
            setHistoryRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));
            historyList = getHistoryListByDeptAndRequester(dept, historyRequester);

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequesterUnivCode, historyRequesterStudentId)
            ).thenReturn(historyRequester);
            when(historyDao.getListByDepartmentAndRequester(
                    univCode, deptCode,
                    historyRequesterUnivCode,
                    historyRequesterStudentId)
            ).thenReturn(historyList);

            System.out.println("Expected : " + historyList);
            TestHelper.listCompareTest(this::execMethod, historyList);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        @Override
        public void ERROR_accessDenied_ForbiddenException() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.BANNED));
            setHistoryRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequesterUnivCode,
                    historyRequesterStudentId)
            ).thenReturn(historyRequester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[ForbiddenException]")
        public void ERROR_getHistoryListOfOthers_ForbiddenException() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));
            setHistoryRequester(randomUserByDeptAndAuthWithExclude(
                    dept, AuthorityDto.Permission.USER, requester));

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequesterUnivCode,
                    historyRequesterStudentId)
            ).thenReturn(historyRequester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`History Requester`의 `index`가 유효하지 않을 시]_[InvalidException]")
        public void ERROR_userInvalidIndex_InvalidException() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.STAFF));
            setHistoryRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequesterUnivCode,
                    historyRequesterStudentId)
            ).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByDeptAndRequester(DepartmentDto dept, UserDto requester) {
            return stub.ALL_HISTORIES.stream()
                    .filter((history) -> dept.matchUniqueKey(history.item().stuff().department())
                            && requester.matchUniqueKey(history.requester()))
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public final class TestGetByIndex extends HistoryNestedTest {
        private HistoryDto history;
        private String stuffName;
        private int itemNum;
        private int historyNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));
            setHistory(randomHistoryByDept(dept));
        }

        private void setHistory(HistoryDto history) {
            this.history = history;
            this.stuffName = history.item().stuff().name();
            this.itemNum = history.item().num();
            this.historyNum = history.num();
        }

        @Override
        protected HistoryDto execMethod() {
            return historyService.getByIndex(
                    userToken, univCode, deptCode,
                    stuffName, itemNum, historyNum
            );
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[본인의 `History`에 대한 `request`일 시]_[-]")
        public void SUCCESS_getHistoryListHerSelf() {
            setUpDefault();
            setRequester(history.requester());

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    univCode, deptCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            System.out.println("Expected : " + history);
            TestHelper.objectCompareTest(this::execMethod, history);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[타인의 `History`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
        public void SUCCESS_getHistoryListOfOthers() {
            setUpDefault();
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.STAFF));

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    univCode, deptCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            System.out.println("Expected : " + history);
            TestHelper.objectCompareTest(this::execMethod, history);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        @Override
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.BANNED));

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    univCode, deptCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[ForbiddenException]")
        public void ERROR_getHistoryListOfOthers_ForbiddenException() {
            setUpDefault();
            setRequester(randomUserByDeptAndAuthWithExclude(
                    dept, AuthorityDto.Permission.USER, history.requester()));

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    univCode, deptCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`index`에 해당하는 `History`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_userInvalidIndex_InvalidException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    univCode, deptCode,
                    stuffName, itemNum, historyNum)
            ).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        private HistoryDto randomHistoryByDept(DepartmentDto dept) {
            RandomFilter<HistoryDto> randomFilter = RandomFilter.makeInstance(stub.ALL_HISTORIES,
                    (history) -> history.item().stuff().department().matchUniqueKey(dept));
            return randomFilter.get().orElse(null);
        }
    }

    @Nested
    @DisplayName("createReservation()")
    public final class TestCreateReservation extends HistoryNestedTest {
        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        @Captor
        private ArgumentCaptor<ItemDto> itemCaptor;

        @Captor
        private ArgumentCaptor<Integer> integerCaptor;

        private ItemDto item;
        private StuffDto stuff;
        private String stuffName;
        private int itemNum;

        private Integer argItemNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(
                    dept, AuthorityDto.Permission.USER));
            setItem(randomNonFirstUsableItemByDept(dept));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuff = item.stuff();
            this.stuffName = stuff.name();
            this.itemNum = item.num();
            this.argItemNum = itemNum;
        }

        @Override
        protected Object execMethod() {
            return historyService.createReservation(userToken,
                    univCode, deptCode, stuffName, argItemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`itemNum`이 `null`이 아닐 시]_[-]")
        public void SUCCESS_itemNumIsNotNull() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).create(historyCaptor.capture());
            verify(itemDao).update(eq(univCode), eq(deptCode),
                    eq(stuffName), eq(itemNum), itemCaptor.capture());

            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyRequester = historyCaptor.getValue().requester();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyRequester).isEqualTo(requester);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`itemNum`이 `null`일 시]_[-]")
        public void SUCCESS_itemNumIsNull() {
            setUpDefault();
            setItem(randomFirstUsableItemByDept(dept));
            argItemNum = null;

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).create(historyCaptor.capture());
            verify(itemDao).update(eq(univCode), eq(deptCode), eq(stuffName),
                    integerCaptor.capture(), itemCaptor.capture());

            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyRequester = historyCaptor.getValue().requester();
            Integer targetItemNum = integerCaptor.getValue();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyRequester).isEqualTo(requester);
            Assertions.assertThat(targetItemNum).isEqualTo(stuff.firstUsableItemNum());
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 대여가 불가능 한 상태일 시 1]_[MethodNotAllowedException]")
        public void ERROR_itemIsUnusable_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomUnusableItemByDept(dept));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 대여가 불가능 한 상태일 시 2]_[MethodNotAllowedException]")
        public void ERROR_itemIsInactive_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomInactiveItemByDept(dept));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`itemNum`이 `null`이고 대여가능 한 `item`이 존재하지 않을 시]_[MethodNotAllowedException]")
        public void ERROR_noUsableItem_MethodNotAllowedException() {
            setUpDefault();
            StuffDto targetStuff = randomUnusableStuffByDept(dept);
            setItem(randomItemByStuff(targetStuff));
            argItemNum = null;

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`가 이미 해당 물품을 " + HistoryService.MAX_LENTAL_COUNT_ON_SAME_STUFF + "개 사용 중일 시]_[MethodNotAllowedException]")
        public void ERROR_requesterAlreadyRequestStuff_MethodNotAllowedException() {
            setUpDefault();
            StuffDto targetStuff = randomStuffWithMoreThanNItemByDept(dept, HistoryService.MAX_LENTAL_COUNT_ON_SAME_STUFF);
            setItem(randomItemByStuff(targetStuff));

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartmentAndRequester(univCode, deptCode, requesterUnivCode, requesterStudentId))
                    .thenReturn(makeHistoryListWithSameStuff());
            when(stuffDao.getByIndex(univCode, deptCode, stuffName)).thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`requester`가 이미 " + HistoryService.MAX_LENTAL_COUNT + "개 이상에 대한 대여가 진행 중일 시]_[MethodNotAllowedException]")
        public void ERROR_requesterHasTooManyRequest_MethodNotAllowedException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartmentAndRequester(univCode, deptCode, requesterUnivCode, requesterStudentId))
                    .thenReturn(makeHistoryListWithSameRequester());
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(univCode, deptCode, stuffName))
                    .thenReturn(stuff);
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private ItemDto randomItemByStuff(StuffDto stuff) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().matchUniqueKey(stuff));
            return randomFilter.get().orElse(null);
        }

        private ItemDto randomItemWithExcludeByStuff(StuffDto stuff, List<ItemDto> exclude) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().matchUniqueKey(stuff)
                            && !exclude.contains(item));
            return randomFilter.get().orElse(null);
        }

        private ItemDto randomFirstUsableItemByDept(DepartmentDto dept) {
            StuffDto targetStuff = RandomFilter.makeInstance(stub.ALL_STUFFS,
                    (stuff) -> stuff.department().matchUniqueKey(dept) && stuff.count() > 0
            ).get().orElse(null);

            return stub.ALL_ITEMS.stream().filter(
                    (item) -> item.stuff().matchUniqueKey(targetStuff)
                            && item.status() == ItemDto.ItemStatus.USABLE
            ).min(Comparator.comparingInt(ItemDto::num)).orElse(null);
        }

        private ItemDto randomNonFirstUsableItemByDept(DepartmentDto dept) {
            StuffDto targetStuff = RandomFilter.makeInstance(stub.ALL_STUFFS,
                    (stuff) -> stuff.department().matchUniqueKey(dept)
                            && stuff.count() > 1
            ).get().orElse(null);

            return RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().matchUniqueKey(targetStuff)
                            && item.status() == ItemDto.ItemStatus.USABLE
                            && item.num() != item.stuff().firstUsableItemNum()
            ).get().orElse(null);
        }

        private StuffDto randomStuffWithMoreThanNItemByDept(DepartmentDto dept, int minItemNum) {
            RandomFilter<StuffDto> randomFilter = RandomFilter.makeInstance(stub.ALL_STUFFS,
                    (stuff) -> stuff.department().matchUniqueKey(dept)
                            && stuff.items().size() > minItemNum
            );
            return randomFilter.get().orElse(null);
        }

        private StuffDto randomUnusableStuffByDept(DepartmentDto dept) {
            RandomFilter<StuffDto> randomFilter = RandomFilter.makeInstance(stub.ALL_STUFFS,
                    (stuff) -> stuff.department().matchUniqueKey(dept)
                            && stuff.firstUsableItemNum() == 0
            );
            return randomFilter.get().orElse(null);
        }

        private StuffDto randomStuffWithExcludeByDept(DepartmentDto dept, List<StuffDto> exclude) {
            RandomFilter<StuffDto> randomFilter = RandomFilter.makeInstance(stub.ALL_STUFFS,
                    (stuff) -> stuff.department().matchUniqueKey(dept)
                            && !exclude.contains(stuff));
            return randomFilter.get().orElse(null);
        }

        private List<HistoryDto> makeHistoryListWithSameStuff() {
            List<HistoryDto> output = new ArrayList<>();
            List<ItemDto> exclude = new ArrayList<>(List.of(item));
            for(int i = 0; i < HistoryService.MAX_LENTAL_COUNT_ON_SAME_STUFF; i++) {
                ItemDto newItem = randomItemWithExcludeByStuff(stuff, exclude);
                exclude.add(newItem);
                output.add(
                        new HistoryDto(
                                newItem,
                                newItem.nextHistoryNum(),
                                requester,
                                null,
                                null,
                                null,
                                null,
                                System.currentTimeMillis()/1000,
                                0,
                                0,
                                0,
                                0
                        )
                );
            }
            return output;
        }

        private List<HistoryDto> makeHistoryListWithSameRequester() {
            List<HistoryDto> output = new ArrayList<>();
            List<StuffDto> exclude = new ArrayList<>(List.of(stuff));
            for(int i = 0; i < HistoryService.MAX_LENTAL_COUNT; i++) {
                StuffDto newStuff = randomStuffWithExcludeByDept(dept, exclude);
                exclude.add(newStuff);

                ItemDto newItem = randomItemByStuff(newStuff);
                output.add(
                        new HistoryDto(
                                newItem,
                                newItem.nextHistoryNum(),
                                requester,
                                null,
                                null,
                                null,
                                null,
                                System.currentTimeMillis()/1000,
                                0,
                                0,
                                0,
                                0
                        )
                );
            }
            return output;
        }
    }

    @Nested
    @DisplayName("makeItemLost()")
    public final class TestMakeItemLost extends HistoryNestedTest {
        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        @Captor
        private ArgumentCaptor<ItemDto> itemCaptor;

        @Captor
        private ArgumentCaptor<Integer> integerCaptor;

        private ItemDto item;
        private String stuffName;
        private int itemNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(dept, AuthorityDto.Permission.STAFF));
            setItem(randomUsableItemByDept(dept));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuffName = item.stuff().name();
            this.itemNum = item.num();
        }

        @Override
        protected Object execMethod() {
            return historyService.makeItemLost(userToken, univCode, deptCode, stuffName, itemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[해당 `item`이 보관 중 분실되었을 시]_[-]")
        public void SUCCESS_itemIsUsable() {
            setUpDefault();
            setItem(randomUsableItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).create(historyCaptor.capture());
            verify(itemDao).update(eq(univCode), eq(deptCode),
                    eq(stuffName), eq(itemNum), itemCaptor.capture());

            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyRequester = historyCaptor.getValue().requester();
            UserDto historyLostManager = historyCaptor.getValue().lostManager();
            long historyLostTimeStamp = historyCaptor.getValue().lostTimeStamp();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyRequester).isEqualTo(null);
            Assertions.assertThat(historyLostManager).isEqualTo(requester);
            Assertions.assertThat(historyLostTimeStamp).isNotZero();
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[해당 `item`이 대여 중 분실 되었을 시]_[-]")
        public void SUCCESS_itemIsUnusable() {
            setUpDefault();
            setItem(randomUnusableItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).update(
                    eq(univCode), eq(deptCode), eq(stuffName), eq(itemNum),
                    integerCaptor.capture(), historyCaptor.capture());

            int historyNum = integerCaptor.getValue();
            UserDto historyLostManager = historyCaptor.getValue().lostManager();
            long historyLostTimestamp = historyCaptor.getValue().lostTimeStamp();

            Assertions.assertThat(historyNum).isEqualTo(item.lastHistory().num());
            Assertions.assertThat(historyLostManager).isEqualTo(requester);
            Assertions.assertThat(historyLostTimestamp).isNotZero();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 이미 분실된 상태일 시]_[MethodNotAllowedException]")
        public void ERROR_itemIsUnusable_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomInactiveItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    @Nested
    @DisplayName("makeItemUsing()")
    public final class TestMakeItemUsing extends HistoryNestedTest {
        @Captor
        private ArgumentCaptor<Integer> integerCaptor;

        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        private ItemDto item;
        private String stuffName;
        private int itemNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(dept, AuthorityDto.Permission.STAFF));
            setItem(randomReservedItemByDept(dept));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuffName = item.stuff().name();
            this.itemNum = item.num();
        }

        @Override
        protected HistoryDto execMethod() {
            return historyService.makeItemUsing(userToken, univCode, deptCode, stuffName, itemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).update(
                    eq(univCode), eq(deptCode), eq(stuffName), eq(itemNum),
                    integerCaptor.capture(), historyCaptor.capture());

            int historyNum = integerCaptor.getValue();
            UserDto historyApprovalManage = historyCaptor.getValue().approveManager();
            long approvalTimestamp = historyCaptor.getValue().approvalTimeStamp();

            Assertions.assertThat(historyNum).isEqualTo(item.lastHistory().num());
            Assertions.assertThat(historyApprovalManage).isEqualTo(requester);
            Assertions.assertThat(approvalTimestamp).isNotZero();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 예약된 상태가 아닐 시]_[MethodNotAllowedException]")
        public void ERROR_itemIsUnusable_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomUsableItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    @Nested
    @DisplayName("makeItemReturn()")
    public final class TestMakeItemReturn extends HistoryNestedTest {
        @Captor
        private ArgumentCaptor<Integer> integerCaptor;

        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        private ItemDto item;
        private String stuffName;
        private int itemNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(dept, AuthorityDto.Permission.STAFF));
            setItem(randomReturnAbleItemByDept(dept));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuffName = item.stuff().name();
            this.itemNum = item.num();
        }

        @Override
        protected HistoryDto execMethod() {
            return historyService.makeItemReturn(userToken, univCode, deptCode, stuffName, itemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).update(
                    eq(univCode), eq(deptCode), eq(stuffName), eq(itemNum),
                    integerCaptor.capture(), historyCaptor.capture());

            int historyNum = integerCaptor.getValue();
            UserDto historyReturnManger = historyCaptor.getValue().returnManager();
            long returnTimeStamp = historyCaptor.getValue().returnTimeStamp();

            Assertions.assertThat(historyNum).isEqualTo(item.lastHistory().num());
            Assertions.assertThat(historyReturnManger).isEqualTo(requester);
            Assertions.assertThat(returnTimeStamp).isNotZero();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 이미 반납되어있는 상태일 시 1]_[MethodNotAllowedException]")
        public void ERROR_itemIsUsable_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomUsableItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 이미 반납되어있는 상태일 시 2]_[MethodNotAllowedException]")
        public void ERROR_itemIsReserved_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomReservedItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private ItemDto randomReturnAbleItemByDept(DepartmentDto dept) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().department().matchUniqueKey(dept)
                            && item.lastHistory() != null
                            && (item.lastHistory().status() == HistoryDto.HistoryStatus.USING
                            || item.lastHistory().status() == HistoryDto.HistoryStatus.DELAYED
                            || item.lastHistory().status() == HistoryDto.HistoryStatus.LOST)
            );
            return randomFilter.get().orElse(null);
        }
    }

    @Nested
    @DisplayName("makeItemCancel()")
    public final class TestMakeItemCancel extends HistoryNestedTest {
        @Captor
        private ArgumentCaptor<Integer> integerCaptor;

        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        private ItemDto item;
        private String stuffName;
        private int itemNum;

        @Override
        protected void setUpDefault() {
            setDept(TEST_DEPT);
            setRequester(randomUserByDeptAndAuth(dept, AuthorityDto.Permission.STAFF));
            setItem(randomReservedItemByDept(dept));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.itemNum = item.num();
            this.stuffName = item.stuff().name();
        }

        @Override
        protected HistoryDto execMethod() {
            return historyService.makeItemCancel(userToken, univCode, deptCode, stuffName, itemNum);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            execMethod();

            verify(historyDao).update(
                    eq(univCode), eq(deptCode), eq(stuffName),
                    eq(itemNum), integerCaptor.capture(), historyCaptor.capture());

            int historyNum = integerCaptor.getValue();
            UserDto historyCancelManager = historyCaptor.getValue().cancelManager();
            long cancelTimeStamp = historyCaptor.getValue().cancelTimeStamp();

            Assertions.assertThat(historyNum).isEqualTo(item.lastHistory().num());
            Assertions.assertThat(historyCancelManager).isEqualTo(requester);
            Assertions.assertThat(cancelTimeStamp).isNotZero();
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 이미 반납되어있는 상태일 시 1]_[MethodNotAllowedException]")
        public void ERROR_itemIsUsable_MethodNotAllowedException() {
            setUpDefault();
            setItem(randomUsableItemByDept(dept));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(univCode, deptCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    private abstract class HistoryNestedTest extends BaseNestedTest {
        protected ItemDto randomUsableItemByDept(DepartmentDto dept) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().department().matchUniqueKey(dept)
                            && item.status() == ItemDto.ItemStatus.USABLE);
            return randomFilter.get().orElse(null);
        }

        protected ItemDto randomUnusableItemByDept(DepartmentDto dept) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().department().matchUniqueKey(dept)
                            && item.status() == ItemDto.ItemStatus.UNUSABLE
            );
            return randomFilter.get().orElse(null);
        }

        protected ItemDto randomInactiveItemByDept(DepartmentDto dept) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().department().matchUniqueKey(dept)
                            && item.status() == ItemDto.ItemStatus.INACTIVE
            );
            return randomFilter.get().orElse(null);
        }

        protected ItemDto randomReservedItemByDept(DepartmentDto dept) {
            RandomFilter<ItemDto> randomFilter = RandomFilter.makeInstance(stub.ALL_ITEMS,
                    (item) -> item.stuff().department().matchUniqueKey(dept)
                            && item.lastHistory() != null
                            && item.lastHistory().status() == HistoryDto.HistoryStatus.REQUESTED
            );
            return randomFilter.get().orElse(null);
        }
    }
}
