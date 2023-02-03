package com.example.beliemeserver.model;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.UserService;
import com.example.beliemeserver.util.RandomFilter;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends BaseServiceTest {
    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("getListByDepartment")
    public final class TestGetListByDepartment extends BaseNestedTestWithDept {
        @Override
        protected void setUpDefault() {
            setDept(randomDept());
            setRequester(randomUserHaveAuthOnDept(dept, AuthorityDto.Permission.MASTER));
        }

        @Override
        protected List<UserDto> execMethod() {
            return userService.getListByDepartment(userToken, univCode, deptCode);
        }

        @Override
        protected void setRequesterAccessDenied() {
            setRequester(randomUserHaveLessAuthOnDept(dept, AuthorityDto.Permission.MASTER));
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUpDefault();

            mockDepartmentAndRequester();
            when(userDao.getAllList()).thenReturn(stub.ALL_USERS);

            List<UserDto> expected = new ArrayList<>();
            for(UserDto user : stub.ALL_USERS) {
                if(user.getMaxPermission(dept).hasMorePermission(AuthorityDto.Permission.USER))
                    expected.add(user);
            }

            TestHelper.listCompareTest(this::execMethod, expected);
        }

        private DepartmentDto randomDept() {
            RandomFilter<DepartmentDto> randomFilter = RandomFilter.makeInstance(
                    stub.ALL_DEPTS, (dept) -> true);
            return randomFilter.get().orElse(null);
        }
    }

    private abstract class UserNestedTest extends BaseNestedTest {
    }
}
