package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.service.UniversityService;
import com.example.beliemeserver.util.RandomFilter;
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

    @Nested
    @DisplayName("create()")
    public class TestCreate extends UnivNestedTest {
        private UniversityDto univ;
        private String code;
        private String name;
        private String apiUrl;


        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setUniv(randomUniv());
        }

        private void setUniv(UniversityDto univ) {
            this.univ = univ;
            this.code = univ.code();
            this.name = univ.name();
            this.apiUrl = univ.apiUrl();
        }

        @Override
        protected UniversityDto execMethod() {
            return universityService.create(userToken, code, name, apiUrl);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void success() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);

            execMethod();

            verify(universityDao).create(univ);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 code가 이미 존재할 시]_[ConflictException]")
        public void universityConflict_ConflictException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(randomDevUser());
            when(universityDao.create(univ)).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate extends UnivNestedTest {
        private UniversityDto targetUniv;
        private String targetCode;

        private String newCode;
        private String newName;
        private String newApiUrl;

        @Override
        protected void setUpDefault() {
            setRequester(randomDevUser());
            setUniv(randomUniv());
        }

        private void setUniv(UniversityDto univ) {
            this.targetUniv = univ;
            this.targetCode = univ.code();

            this.newCode = univ.code() + "AAA";
            this.newName = univ.name() + "BBB";
            this.newApiUrl = univ.apiUrl() + "CCC";
        }

        @Override
        protected UniversityDto execMethod() {
            return universityService.update(userToken, targetCode, newCode, newName, newApiUrl);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[모든 멤버 변경 시]_[-]")
        public void success() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetCode)).thenReturn(targetUniv);

            execMethod();

            UniversityDto newUniv = new UniversityDto(newCode, newName, newApiUrl);
            verify(universityDao).update(targetCode, newUniv);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[newUniversityCode, newName이 null일 때]_[-]")
        public void success_newUniversityCodeIsNull() {
            setUpDefault();
            newCode = null;
            newName = null;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetCode)).thenReturn(targetUniv);

            execMethod();

            UniversityDto newUniv = new UniversityDto(targetCode, targetUniv.name(), newApiUrl);
            verify(universityDao).update(targetCode, newUniv);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[newApiUrl이 null일 때]_[-]")
        public void success_newApiUrlIsNull() {
            setUpDefault();
            newApiUrl = null;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetCode)).thenReturn(targetUniv);

            execMethod();

            UniversityDto newUniv = new UniversityDto(newCode, newName, targetUniv.apiUrl());
            verify(universityDao).update(targetCode, newUniv);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[모든 member가 null일 때]_[-]")
        public void success_allNewMemberIsNull() {
            setUpDefault();
            newCode = null;
            newName = null;
            newApiUrl = null;

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetCode)).thenReturn(targetUniv);

            execMethod();

            verify(universityDao, never()).update(eq(targetCode), any());
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[target이 존재하지 않을 시]_[NotFoundException]")
        public void universityNotFound_NotFoundException() {
            setUpDefault();

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetCode)).thenThrow(NotFoundException.class);

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[해당 code가 이미 존재할 시]_[ConflictException]")
        public void universityConflict_ConflictException() {
            setUpDefault();
            UniversityDto newUniv = new UniversityDto(newCode, newName, newApiUrl);

            when(userDao.getByToken(userToken)).thenReturn(requester);
            when(universityDao.getByIndex(targetCode)).thenReturn(targetUniv);
            when(universityDao.update(targetCode, newUniv)).thenThrow(ConflictException.class);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
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

        protected UniversityDto randomUniv() {
            RandomFilter<UniversityDto> randomFilter = RandomFilter.makeInstance(stub.ALL_UNIVS, (univ) -> true);
            return randomFilter.get().orElse(null);
        }
    }
}
