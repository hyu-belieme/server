package com.example.beliemeserver.data;

import com.example.beliemeserver.data.daoimpl._new.NewUniversityDaoImpl;
import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.util.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewUniversityDaoTest extends BaseDaoTest {
    @InjectMocks
    private NewUniversityDaoImpl universityDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<UniversityDto> execMethod() {
            return universityDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(univRepository.findAll()).thenReturn(stub.ALL_UNIVS);
            TestHelper.listCompareTest(this::execMethod, toUniversityDtoList(stub.ALL_UNIVS));
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById {
        private NewUniversityEntity univ;
        private UUID univId;

        private void setUniv(NewUniversityEntity univ) {
            this.univ = univ;
            this.univId = univ.getId();
        }

        protected UniversityDto execMethod() {
            return universityDao.getById(univId);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUniv(randomUniv());

            when(univRepository.findById(univId)).thenReturn(Optional.of(univ));
            TestHelper.objectCompareTest(this::execMethod, univ.toUniversityDto());
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`id`에 해당하는 `university`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_NotFound_NotFoundException() {
            setUniv(randomUniv());

            when(univRepository.findById(univId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("checkExistByIndex()")
    public class TestCheckByIndex {
        private final String univName = "";

        protected boolean execMethod() {
            return universityDao.checkExistByIndex(univName);
        }

        @Test()
        @DisplayName("[SUCCESS]_[공통된 이름을 가진 `university`가 존재할 시]_[-]")
        public void SUCCESS_exist() {
            when(univRepository.existsByName(univName)).thenReturn(true);

            TestHelper.objectCompareTest(this::execMethod, true);
        }

        @Test()
        @DisplayName("[SUCCESS]_[공통된 이름을 가진 `university`가 존재하지 않을 시]_[-]")
        public void SUCCESS_notExist() {
            when(univRepository.existsByName(univName)).thenReturn(false);

            TestHelper.objectCompareTest(this::execMethod, false);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private NewUniversityEntity univ;

        private void setUniv(NewUniversityEntity univ) {
            this.univ = univ;
        }

        protected UniversityDto execMethod() {
            return universityDao.create(univ.toUniversityDto());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setUniv(randomUniv());

            when(univRepository.existsByName(univ.getName())).thenReturn(false);
            when(univRepository.save(univ)).thenReturn(univ);

            TestHelper.objectCompareTest(this::execMethod, univ.toUniversityDto());

            verify(univRepository).save(univ);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `name`을 갖는 `university`가 존재할 시]_[ConflictException]")
        public void ERROR_NameConflict_ConflictException() {
            setUniv(randomUniv());

            when(univRepository.existsByName(univ.getName())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        private NewUniversityEntity targetUniv;
        private UUID targetId;
        private UniversityDto newUnivDto;

        private void setTargetUniv(NewUniversityEntity univ) {
            this.targetUniv = univ;
            this.targetId = targetUniv.getId();
        }

        private NewUniversityEntity savedUniv() {
            return targetUniv
                    .withName(newUnivDto.name())
                    .withApiUrl(newUnivDto.apiUrl());
        }

        protected UniversityDto execMethod() {
            return universityDao.update(targetId, newUnivDto);
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[index(name)을 변경하지 않을 시]_[-]")
        public void SUCCESS_notChangeIndex() {
            setTargetUniv(randomUniv());
            newUnivDto = targetUniv.toUniversityDto()
                    .withApiUrl("https://NewApiUrl.com");

            when(univRepository.findById(targetId)).thenReturn(Optional.of(targetUniv));
            when(univRepository.save(savedUniv())).thenReturn(savedUniv());

            TestHelper.objectCompareTest(this::execMethod, savedUniv().toUniversityDto());

            verify(univRepository).save(savedUniv());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[index(name)를 변경할 시]_[]")
        public void SUCCESS_changeIndex() {
            setTargetUniv(randomUniv());
            newUnivDto = targetUniv.toUniversityDto()
                    .withName("__NEW_NAME")
                    .withApiUrl("https://NewApiUrl.com");

            when(univRepository.findById(targetId)).thenReturn(Optional.of(targetUniv));
            when(univRepository.existsByName(newUnivDto.name())).thenReturn(false);
            when(univRepository.save(savedUniv())).thenReturn(savedUniv());

            TestHelper.objectCompareTest(this::execMethod, savedUniv().toUniversityDto());

            verify(univRepository).save(savedUniv());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[`NewUniversity`의 id는 상관 없다]_[]")
        public void SUCCESS_notSameIdOnNewUniversity() {
            setTargetUniv(randomUniv());
            newUnivDto = randomUniv().toUniversityDto()
                    .withName("__NEW_NAME")
                    .withApiUrl("https://NewApiUrl.com");

            when(univRepository.findById(targetId)).thenReturn(Optional.of(targetUniv));
            when(univRepository.existsByName(newUnivDto.name())).thenReturn(false);
            when(univRepository.save(savedUniv())).thenReturn(savedUniv());

            TestHelper.objectCompareTest(this::execMethod, savedUniv().toUniversityDto());

            verify(univRepository).save(savedUniv());
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[index(name)를 변경했는데 해당 index를 갖는 `university`가 이미 존재할 시]_[ConflictException]")
        public void ERROR_changeIndexAlreadyExist_ConflictException() {
            setTargetUniv(randomUniv());
            newUnivDto = targetUniv.toUniversityDto()
                    .withName("__NEW_NAME")
                    .withApiUrl("https://NewApiUrl.com");

            when(univRepository.findById(targetId)).thenReturn(Optional.of(targetUniv));
            when(univRepository.existsByName(newUnivDto.name())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }
}
