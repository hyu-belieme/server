package com.belieme.apiserver.data;

import com.belieme.apiserver.domain.dto.MajorDto;
import com.belieme.apiserver.error.exception.ConflictException;
import com.belieme.apiserver.error.exception.InvalidIndexException;
import com.belieme.apiserver.error.exception.NotFoundException;
import com.belieme.apiserver.util.TestHelper;
import com.belieme.apiserver.data.daoimpl.MajorDaoImpl;
import com.belieme.apiserver.data.entity.MajorEntity;
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
public class MajorDaoTest extends BaseDaoTest {
    @InjectMocks
    private MajorDaoImpl majorDao;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        protected List<MajorDto> execMethod() {
            return majorDao.getAllList();
        }

        @Test()
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            when(majorRepository.findAll()).thenReturn(stub.ALL_MAJORS);
            TestHelper.listCompareTest(this::execMethod, toMajorDtoList(stub.ALL_MAJORS));
        }
    }

    @Nested
    @DisplayName("getById()")
    public class TestGetById {
        private MajorEntity major;
        private UUID majorId;

        private void setMajor(MajorEntity major) {
            this.major = major;
            this.majorId = major.getId();
        }

        protected MajorDto execMethod() {
            return majorDao.getById(majorId);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setMajor(randomMajor());

            when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));
            TestHelper.objectCompareTest(this::execMethod, major.toMajorDto());
        }

        @Test
        @DisplayName("[ERROR]_[`id`에 해당하는 `major`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setMajor(randomMajor());

            when(majorRepository.findById(majorId)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex {
        private MajorEntity major;
        private UUID univId;
        private String majorCode;

        private void setMajor(MajorEntity major) {
            this.major = major;
            this.univId = major.getUniversityId();
            this.majorCode = major.getCode();
        }

        protected MajorDto execMethod() {
            return majorDao.getByIndex(univId, majorCode);
        }

        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setMajor(randomMajor());

            when(majorRepository.findByUniversityIdAndCode(univId, majorCode)).thenReturn(Optional.of(major));
            TestHelper.objectCompareTest(this::execMethod, major.toMajorDto());
        }

        @Test
        @DisplayName("[ERROR]_[`index`에 해당하는 `major`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_notFound_NotFoundException() {
            setMajor(randomMajor());

            when(majorRepository.findByUniversityIdAndCode(univId, majorCode)).thenReturn(Optional.empty());
            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        private MajorEntity major;

        private void setMajor(MajorEntity major) {
            this.major = major;
        }

        protected MajorDto execMethod() {
            return majorDao.create(major.getId(), major.getUniversityId(), major.getCode());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setMajor(randomMajor());

            when(univRepository.findById(major.getUniversityId())).thenReturn(Optional.of(major.getUniversity()));
            when(majorRepository.existsById(major.getId())).thenReturn(false);
            when(majorRepository.existsByUniversityIdAndCode(major.getUniversityId(), major.getCode())).thenReturn(false);
            when(majorRepository.save(mockArg(major))).thenReturn(major);

            TestHelper.objectCompareTest(this::execMethod, major.toMajorDto());

            verify(majorRepository).save(mockArg(major));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`universityId`에 해당하는 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_universityNotFound_InvalidIndexException() {
            setMajor(randomMajor());

            when(univRepository.findById(major.getUniversityId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `id`를 갖는 `major`가 존재할 시]_[ConflictException]")
        public void ERROR_idConflict_ConflictException() {
            setMajor(randomMajor());

            when(univRepository.findById(major.getUniversityId())).thenReturn(Optional.of(major.getUniversity()));
            when(majorRepository.existsById(major.getId())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `major`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setMajor(randomMajor());

            when(univRepository.findById(major.getUniversityId())).thenReturn(Optional.of(major.getUniversity()));
            when(majorRepository.existsById(major.getId())).thenReturn(false);
            when(majorRepository.existsByUniversityIdAndCode(major.getUniversityId(), major.getCode())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        private MajorEntity target;
        private UUID targetId;
        private MajorEntity newMajor;

        private void setTarget(MajorEntity target) {
            this.target = target;
            this.targetId = target.getId();
        }

        private MajorEntity updatedMajor() {
            return target
                    .withUniversity(newMajor.getUniversity())
                    .withCode(newMajor.getCode());
        }

        protected MajorDto execMethod() {
            return majorDao.update(targetId, newMajor.getUniversityId(), newMajor.getCode());
        }

        @RepeatedTest(10)
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void SUCCESS() {
            setTarget(randomMajor());
            newMajor = randomMajor()
                    .withUniversity(randomUniv())
                    .withCode("__NEW_CODE");

            when(majorRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newMajor.getUniversityId())).thenReturn(Optional.of(newMajor.getUniversity()));
            when(majorRepository.existsByUniversityIdAndCode(newMajor.getUniversityId(), newMajor.getCode())).thenReturn(false);
            when(majorRepository.save(mockArg(updatedMajor()))).thenReturn(updatedMajor());

            TestHelper.objectCompareTest(this::execMethod, updatedMajor().toMajorDto());

            verify(majorRepository).save(mockArg(updatedMajor()));
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`id`에 해당하는 `major`가 존재하지 않을 시]_[NotFoundException]")
        public void ERROR_majorNotFound_NotFoundException() {
            setTarget(randomMajor());
            newMajor = randomMajor()
                    .withUniversity(randomUniv())
                    .withCode("__NEW_CODE");

            when(majorRepository.findById(targetId)).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, NotFoundException.class);
        }


        @RepeatedTest(10)
        @DisplayName("[ERROR]_[`universityId`에 해당하는 `university`가 존재하지 않을 시]_[InvalidIndexException]")
        public void ERROR_universityNotFound_InvalidIndexException() {
            setTarget(randomMajor());
            newMajor = randomMajor()
                    .withUniversity(randomUniv())
                    .withCode("__NEW_CODE");

            when(majorRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newMajor.getUniversityId())).thenReturn(Optional.empty());

            TestHelper.exceptionTest(this::execMethod, InvalidIndexException.class);
        }

        @RepeatedTest(10)
        @DisplayName("[ERROR]_[동일한 `index`를 갖는 `major`가 존재할 시]_[ConflictException]")
        public void ERROR_IndexConflict_ConflictException() {
            setTarget(randomMajor());
            newMajor = randomMajor()
                    .withUniversity(randomUniv())
                    .withCode("__NEW_CODE");

            when(majorRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(univRepository.findById(newMajor.getUniversityId())).thenReturn(Optional.of(newMajor.getUniversity()));
            when(majorRepository.existsByUniversityIdAndCode(newMajor.getUniversityId(), newMajor.getCode())).thenReturn(true);

            TestHelper.exceptionTest(this::execMethod, ConflictException.class);
        }
    }
}
