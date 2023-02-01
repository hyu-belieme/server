package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.service.HistoryService;
import com.example.beliemeserver.util.NewStubHelper;
import com.example.beliemeserver.util.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest extends BaseServiceTest {
    public static final String HYU = "HYU";
    public static final String CSE = "CSE";
    NewStubHelper stub = new NewStubHelper();

    @InjectMocks
    private HistoryService historyService;

    @Nested
    @DisplayName("getListByDepartment")
    public final class TestGetListByDepartment extends BaseNestedTestClass {
        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.STAFF));

            historyList = getHistoryListByDepartmentFromStub();
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER));
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByDepartment(userToken, universityCode, departmentCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartment(universityCode, departmentCode))
                    .thenReturn(historyList);

            TestHelper.listCompareTest(
                    this::execMethod,
                    execMethod()
            );
        }

        private List<HistoryDto> getHistoryListByDepartmentFromStub() {
            List<HistoryDto> output = new ArrayList<>();
            for (HistoryDto history : stub.ALL_HISTORIES) {
                if (department.matchUniqueKey(history.item().stuff().department())) {
                    output.add(history);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getListByStuff()")
    public final class TestGetListByStuff extends BaseNestedTestClass {
        private StuffDto stuff;
        private String stuffName;
        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            stuff = stub.ALL_STUFFS.get(0);
            stuffName = stuff.name();
            setDepartment(stuff.department());
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.STAFF));

            historyList = getHistoryListByStuffFromStub();
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER));
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByStuff(
                    userToken, universityCode, departmentCode, stuffName);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByStuff(universityCode, departmentCode, stuffName))
                    .thenReturn(historyList);

            TestHelper.listCompareTest(
                    this::execMethod,
                    execMethod()
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByStuff(universityCode, departmentCode, stuffName))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByStuffFromStub() {
            List<HistoryDto> output = new ArrayList<>();
            for (HistoryDto history : stub.ALL_HISTORIES) {
                if (stuff.matchUniqueKey(history.item().stuff())) {
                    output.add(history);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getListByItem()")
    public final class TestGetListByItem extends BaseNestedTestClass {
        private ItemDto item;
        private String stuffName;
        private int itemNum;

        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            item = stub.ALL_ITEMS.get(0);
            stuffName = item.stuff().name();
            itemNum = item.num();

            setDepartment(item.stuff().department());
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.STAFF));

            historyList = getHistoryListByItemFromStub();
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(stub.getUserByDeptAndAuthWithExclude(
                    universityCode, departmentCode, AuthorityDto.Permission.USER, requester));
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByItem(
                    userToken, universityCode,
                    departmentCode, stuffName, itemNum);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByItem(universityCode, departmentCode, stuffName, itemNum))
                    .thenReturn(historyList);

            TestHelper.listCompareTest(
                    this::execMethod,
                    historyList
            );
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `item`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getListByItem(universityCode, departmentCode, stuffName, itemNum))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByItemFromStub() {
            List<HistoryDto> output = new ArrayList<>();
            for (HistoryDto history : stub.ALL_HISTORIES) {
                if (item.matchUniqueKey(history.item())) {
                    output.add(history);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getListByDepartmentAndRequester()")
    public final class TestGetListByDepartmentAndRequester extends BaseNestedTestClass {
        private UserDto historyRequester;

        private List<HistoryDto> historyList;

        @Override
        protected void setUpDefault() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER));

            historyRequester = requester;
            historyList = getHistoryListByDepartmentAndRequesterFromStub();
        }

        @Override
        protected List<HistoryDto> execMethod() {
            return historyService.getListByDepartmentAndRequester(
                    userToken, universityCode, departmentCode,
                    historyRequester.university().code(),
                    historyRequester.studentId()
            );
        }

        @Test
        @DisplayName("[SUCCESS]_[본인의 `History List`에 대한 `request`일 시]_[-]")
        public void SUCCESS_getHistoryListHerSelf() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(), historyRequester.studentId())
            ).thenReturn(historyRequester);
            when(historyDao.getListByDepartmentAndRequester(
                    universityCode, departmentCode,
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyList);

            TestHelper.listCompareTest(this::execMethod, historyList);
        }

        @Test
        @DisplayName("[SUCCESS]_[타인의 `History List`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
        public void SUCCESS_getHistoryListOfOthers() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.STAFF));
            historyRequester = stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER);
            historyList = getHistoryListByDepartmentAndRequesterFromStub();

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyRequester);
            when(historyDao.getListByDepartmentAndRequester(
                    universityCode, departmentCode,
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyList);

            TestHelper.listCompareTest(this::execMethod, historyList);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        @Override
        public void ERROR_accessDenied_ForbiddenException() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.BANNED));
            historyRequester = stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER);

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyRequester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[ForbiddenException]")
        public void ERROR_getHistoryListOfOthers_ForbiddenException() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER));
            historyRequester = stub.getUserByDeptAndAuthWithExclude(
                    universityCode, departmentCode, AuthorityDto.Permission.USER, requester);

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenReturn(historyRequester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`History Requester`의 `index`가 유효하지 않을 시]_[InvalidException]")
        public void ERROR_userInvalidIndex_InvalidException() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.STAFF));
            historyRequester = stub.getUserByDeptAndAuthWithExclude(
                    universityCode, departmentCode, AuthorityDto.Permission.USER, requester);

            mockDepartmentAndRequester();
            when(userDao.getByIndex(
                    historyRequester.university().code(),
                    historyRequester.studentId())
            ).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private List<HistoryDto> getHistoryListByDepartmentAndRequesterFromStub() {
            List<HistoryDto> output = new ArrayList<>();
            for (HistoryDto history : stub.ALL_HISTORIES) {
                if (department.matchUniqueKey(history.item().stuff().department())
                        && historyRequester.matchUniqueKey(history.requester())) {
                    output.add(history);
                }
            }
            return output;
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public final class TestGetByIndex extends BaseNestedTestClass {
        private HistoryDto history;
        private String stuffName;
        private int itemNum;
        private int historyNum;

        @Override
        protected void setUpDefault() {
            history = stub.ALL_HISTORIES.get(0);
            stuffName = history.item().stuff().name();
            itemNum = history.item().num();
            historyNum = history.num();

            setDepartment(history.item().stuff().department());
            setRequester(stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.USER));
        }

        @Override
        protected HistoryDto execMethod() {
            return historyService.getByIndex(
                    userToken, universityCode, departmentCode,
                    stuffName, itemNum, historyNum
            );
        }

        @Test
        @DisplayName("[SUCCESS]_[본인의 `History`에 대한 `request`일 시]_[-]")
        public void SUCCESS_getHistoryListHerSelf() {
            setUpDefault();
            requester = history.requester();

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    universityCode, departmentCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            TestHelper.objectCompareTest(this::execMethod, history);
        }

        @Test
        @DisplayName("[SUCCESS]_[타인의 `History`에 대한 `request`가 아니지만 `requester`가 `staff` 이상의 권한을 가질 시]_[-]")
        public void SUCCESS_getHistoryListOfOthers() {
            setUpDefault();
            requester = stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.STAFF);

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    universityCode, departmentCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            TestHelper.objectCompareTest(this::execMethod, history);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        @Override
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            requester = stub.getUserByDeptAndAuth(
                    universityCode, departmentCode, AuthorityDto.Permission.BANNED);

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    universityCode, departmentCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[본인의 `History List`에 대한 `request`가 아닐 시]_[ForbiddenException]")
        public void ERROR_getHistoryListOfOthers_ForbiddenException() {
            setUpDefault();
            requester = stub.getUserByDeptAndAuthWithExclude(
                    universityCode, departmentCode, AuthorityDto.Permission.USER, history.requester());

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    universityCode, departmentCode,
                    stuffName, itemNum, historyNum)
            ).thenReturn(history);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`index`에 해당하는 `History`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_userInvalidIndex_InvalidException() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(historyDao.getByIndex(
                    universityCode, departmentCode,
                    stuffName, itemNum, historyNum)
            ).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("createReservation()")
    public final class TestCreateReservation extends BaseNestedTestClass {
        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        @Captor
        private ArgumentCaptor<ItemDto> itemCaptor;

        private String stuffName;
        private Integer itemNum;
        private ItemDto item;
        private StuffDto stuff;

        @Override
        protected void setUpDefault() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(universityCode, departmentCode, AuthorityDto.Permission.USER));
            setItem(stub.getNthUsableItem(HYU, CSE, "우산", 2));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.stuff = item.stuff();
            this.itemNum = item.num();
            this.stuffName = stuff.name();
        }

        @Override
        protected Object execMethod() {
            return historyService.createReservation(userToken, universityCode, departmentCode, stuffName, itemNum);
        }

        @Test
        @DisplayName("[SUCCESS]_[`itemNum`이 `null`이 아닐 시]_[-]")
        public void SUCCESS_itemNumIsNotNull() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenReturn(item);

            execMethod();

            verify(historyDao).create(historyCaptor.capture());
            verify(itemDao).update(eq(universityCode), eq(departmentCode), eq(stuffName), eq(itemNum), itemCaptor.capture());

            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyRequester = historyCaptor.getValue().requester();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyRequester).isEqualTo(requester);
        }

        @Test
        @DisplayName("[SUCCESS]_[`itemNum`이 `null`일 시]_[-]")
        public void SUCCESS_itemNumIsNull() {
            setUpDefault();
            setItem(stub.getNthUsableItem(universityCode, departmentCode, "우산", 1));
            itemNum = null;

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, stuff.firstUsableItemNum())).thenReturn(item);

            execMethod();

            verify(historyDao).create(historyCaptor.capture());
            verify(itemDao).update(eq(universityCode), eq(departmentCode), eq(stuffName), eq(stuff.firstUsableItemNum()), itemCaptor.capture());

            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyRequester = historyCaptor.getValue().requester();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyRequester).isEqualTo(requester);
        }

        @Test
        @DisplayName("[ERROR]_[`item`이 대여가 불가능 한 상태일 시 1]_[MethodNotAllowedException]")
        public void ERROR_itemIsUnusable_MethodNotAllowedException() {
            setUpDefault();
            setItem(stub.getNthUnusableItem(universityCode, departmentCode, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`item`이 대여가 불가능 한 상태일 시 2]_[MethodNotAllowedException]")
        public void ERROR_itemIsInactive_MethodNotAllowedException() {
            setUpDefault();
            setItem(stub.getNthInactiveItem(universityCode, departmentCode, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`itemNum`이 `null`이고 대여가능 한 `item`이 존재하지 않을 시]_[MethodNotAllowedException]")
        public void ERROR_noUsableItem_MethodNotAllowedException() {
            setUpDefault();
            setItem(stub.getNthUnusableItem(universityCode, departmentCode, "블루투스 스피커", 1));
            itemNum = null;

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`requester`가 이미 해당 물품을 " + HistoryService.MAX_LENTAL_COUNT_ON_SAME_STUFF + "개 사용 중일 시]_[MethodNotAllowedException]")
        public void ERROR_requesterAlreadyRequestStuff_MethodNotAllowedException() {
            setUpDefault();
            setItem(stub.getNthUsableItem(universityCode, departmentCode, "우산", 1));

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartmentAndRequester(universityCode, departmentCode, requester.university().code(), requester.studentId()))
                    .thenReturn(makeListWithSameStuffHistory());
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`requester`가 이미 " + HistoryService.MAX_LENTAL_COUNT + "개 이상에 대한 대여가 진행 중일 시]_[MethodNotAllowedException]")
        public void ERROR_requesterHasTooManyRequest_MethodNotAllowedException() {
            setUpDefault();
            setItem(stub.getNthUsableItem(universityCode, departmentCode, "우산", 1));

            mockDepartmentAndRequester();
            when(historyDao.getListByDepartmentAndRequester(universityCode, departmentCode, requester.university().code(), requester.studentId()))
                    .thenReturn(makeListWithSameRequester());
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`stuff`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_stuffInvalidIndex_InvalidIndexException() {
            setUpDefault();
            setItem(stub.getNthInactiveItem(universityCode, departmentCode, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();
            setItem(stub.getNthInactiveItem(universityCode, departmentCode, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(stuffDao.getByIndex(universityCode, departmentCode, stuffName)).thenReturn(stuff);
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        private ArrayList<HistoryDto> makeListWithSameStuffHistory() {
            return new ArrayList<>(List.of(
                    new HistoryDto(
                            stub.getAnotherItemWithSameStuff(item),
                            item.nextHistoryNum(),
                            requester,
                            requester,
                            null,
                            null,
                            null,
                            1,
                            1,
                            0,
                            0,
                            0
                    )
            ));
        }

        private List<HistoryDto> makeListWithSameRequester() {
            List<HistoryDto> output = new ArrayList<>();
            for(int i = 0; i < HistoryService.MAX_LENTAL_COUNT; i++) {
                StuffDto tmpStuff = stub.getNthAnotherStuffWithSameDepartment(item.stuff(), i+1);
                ItemDto tmpItem = stub.getItemByIndex(
                        tmpStuff.department().university().code(),
                        tmpStuff.department().code(),
                        tmpStuff.name(),
                        1
                );
                HistoryDto tmpHistory = new HistoryDto(
                        stub.getItemByIndex(
                                stub.getNthAnotherStuffWithSameDepartment(item.stuff(), 1).department().university().code(),
                                stub.getNthAnotherStuffWithSameDepartment(item.stuff(), 1).department().code(),
                                stub.getNthAnotherStuffWithSameDepartment(item.stuff(), 1).name(),
                                1
                        ),
                        item.nextHistoryNum(),
                        requester,
                        requester,
                        null,
                        null,
                        null,
                        1,
                        1,
                        0,
                        0,
                        0
                );
                output.add(tmpHistory);
            }
            return output;
        }
    }

    @Nested
    @DisplayName("makeItemLost()")
    public final class TestMakeItemLost extends BaseNestedTestClass {
        @Captor
        private ArgumentCaptor<Integer> integerCaptor;

        @Captor
        private ArgumentCaptor<HistoryDto> historyCaptor;

        @Captor
        private ArgumentCaptor<ItemDto> itemCaptor;

        private String stuffName;
        private Integer itemNum;
        private ItemDto item;

        @Override
        protected void setUpDefault() {
            setDepartment(stub.getDeptByIdx(HYU, CSE));
            setRequester(stub.getUserByDeptAndAuth(universityCode, departmentCode, AuthorityDto.Permission.STAFF));
            setItem(stub.getNthUsableItem(HYU, CSE, "우산", 2));
        }

        private void setItem(ItemDto item) {
            this.item = item;
            this.itemNum = item.num();
            this.stuffName = item.stuff().name();
        }

        @Override
        protected Object execMethod() {
            return historyService.makeItemLost(userToken, universityCode, departmentCode, stuffName, itemNum);
        }

        @Test
        @DisplayName("[SUCCESS]_[해당 `item`이 보관 중 분실되었을 시]_[-]")
        public void SUCCESS_itemIsUsable() {
            setUpDefault();
            setItem(stub.getNthUsableItem(HYU, CSE, "우산", 1));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenReturn(item);

            execMethod();

            verify(historyDao).create(historyCaptor.capture());
            verify(itemDao).update(eq(universityCode), eq(departmentCode), eq(stuffName), eq(itemNum), itemCaptor.capture());

            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyRequester = historyCaptor.getValue().requester();
            UserDto historyLostManager = historyCaptor.getValue().lostManager();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyRequester).isEqualTo(null);
            Assertions.assertThat(historyLostManager).isEqualTo(requester);
        }

        @Test
        @DisplayName("[SUCCESS]_[해당 `item`이 대여 중 분실 되었을 시]_[-]")
        public void SUCCESS_itemIsUnusable() {
            setUpDefault();
            setItem(stub.getNthUnusableItem(HYU, CSE, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenReturn(item);
            when(historyDao.getByIndex(universityCode, departmentCode, stuffName, itemNum, item.lastHistory().num()))
                    .thenReturn(stub.getHistoryByIndex(universityCode, departmentCode, stuffName, itemNum, item.lastHistory().num()));

            execMethod();

            verify(historyDao).update(eq(universityCode), eq(departmentCode), eq(stuffName), eq(itemNum), integerCaptor.capture(), historyCaptor.capture());

            int historyNum = integerCaptor.getValue();
            ItemDto historyItem = historyCaptor.getValue().item();
            UserDto historyLostManager = historyCaptor.getValue().lostManager();

            Assertions.assertThat(historyItem).isEqualTo(item);
            Assertions.assertThat(historyNum).isEqualTo(item.lastHistory().num());
            Assertions.assertThat(historyLostManager).isEqualTo(requester);
        }

        @Test
        @DisplayName("[ERROR]_[`item`이 이미 분실된 상태일 시]_[MethodNotAllowedException]")
        public void ERROR_itemIsUnusable_MethodNotAllowedException() {
            setUpDefault();
            setItem(stub.getNthInactiveItem(universityCode, departmentCode, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenReturn(item);

            TestHelper.exceptionTest(this::execMethod, MethodNotAllowedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[`item`이 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_itemInvalidIndex_InvalidIndexException() {
            setUpDefault();
            setItem(stub.getNthInactiveItem(universityCode, departmentCode, "블루투스 스피커", 1));

            mockDepartmentAndRequester();
            when(itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }
    }

    private abstract class BaseNestedTestClass {
        protected DepartmentDto department;
        protected String universityCode;
        protected String departmentCode;

        protected UserDto requester;

        protected abstract void setUpDefault();
        protected abstract Object execMethod();

        protected void setDepartment(DepartmentDto department) {
            this.department = department;
            this.universityCode = department.university().code();
            this.departmentCode = department.code();
        }

        protected void setRequester(UserDto requester) {
            this.requester = requester;
        }

        protected void setRequesterAccessDenied() {
            requester = stub.HYU_CSE_BANNED_USER;
        }

        protected void mockDepartmentAndRequester() {
            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenReturn(requester);
        }

        @Test
        @DisplayName("[ERROR]_[해당 `index`의 `department`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_getInvalidIndex_InvalidIndexException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void ERROR_isUnauthorizedToken_UnauthorizedException() {
            setUpDefault();

            when(departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(universityCode, departmentCode))
                    .thenReturn(department);
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, UnauthorizedException.class);
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void ERROR_accessDenied_ForbiddenException() {
            setUpDefault();
            setRequesterAccessDenied();

            mockDepartmentAndRequester();

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
