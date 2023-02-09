package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.service.UniversityService;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UniversityServiceTest extends BaseServiceTest {
    @InjectMocks
    private UniversityService universityService;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList extends UnivNestedTest {
        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
        }

        @Override
        protected List<UniversityDto> execMethod() {
            return universityService.getAllList(userToken);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void success() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getAllList()).thenReturn(stub.ALL_UNIVS);

            TestHelper.listCompareTest(this::execMethod, stub.ALL_UNIVS);
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex extends UnivNestedTest {
        private UniversityDto univ;
        private String univCode;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setUniv(randomUniv());
        }

        private void setUniv(UniversityDto univ) {
            this.univ = univ;
            this.univCode = univ.name();
        }

        @Override
        protected UniversityDto execMethod() {
            return universityService.getByIndex(userToken, univCode);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void success() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(univCode)).thenReturn(univ);

            TestHelper.objectCompareTest(this::execMethod, univ);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 code에 university가 존재하지 않을 시]_[NotFoundException]")
        public void universityNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(univCode)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    private UniversityDto randomUniv() {
        return randomSelectAndLog(allUnivs());
    }

    private abstract class UnivNestedTest extends BaseNestedTest {
        @RepeatedTest(10)
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void accessDenied_ForbiddenException() {
            setUpDefault();
            setRequester(randomNonDevUser());

            when(userDao.getByToken(userToken)).thenReturn(requester);

            TestHelper.exceptionTest(this::execMethod, ForbiddenException.class);
        }
    }
}
