package com.example.beliemeserver.model;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.service.UniversityService;
import com.example.beliemeserver.util.StubHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UniversityServiceTest extends BaseServiceTest {
    @InjectMocks
    private UniversityService universityService;

    @Nested
    @DisplayName("getAllList()")
    public class TestGetAllList {
        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void success() {
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);

            universityService.getAllList(userToken);

            verify(universityDao).getAllUniversitiesData();
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void isUnauthorizedToken_UnauthorizedException() {
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            Assertions.assertThrows(UnauthorizedException.class, () -> universityService.getAllList(userToken));
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void accessDenied_ForbiddenException() {
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.masterOfDepartment1);

            Assertions.assertThrows(ForbiddenException.class, () -> universityService.getAllList(userToken));
        }
    }

    @Nested
    @DisplayName("getByIndex()")
    public class TestGetByIndex {
        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void success() {
            String universityCode = "HYU";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);

            universityService.getByIndex(userToken, universityCode);

            verify(universityDao).getUniversityByCodeData(universityCode);
        }

        @Test
        @DisplayName("[ERROR]_[해당 code에 university가 존재하지 않을 시]_[NotFoundException]")
        public void universityNotFound_NotFoundException() {
            String universityCode = "HYU";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.getUniversityByCodeData(universityCode)).thenThrow(NotFoundException.class);

            Assertions.assertThrows(NotFoundException.class, () -> universityService.getByIndex(userToken, universityCode));
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void isUnauthorizedToken_UnauthorizedException() {
            String universityCode = "HYU";
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            Assertions.assertThrows(UnauthorizedException.class, () -> universityService.getByIndex(userToken, universityCode));
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void accessDenied_ForbiddenException() {
            String universityCode = "HYU";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.masterOfDepartment1);

            Assertions.assertThrows(ForbiddenException.class, () -> universityService.getByIndex(userToken, universityCode));
        }
    }

    @Nested
    @DisplayName("create()")
    public class TestCreate {
        @Test
        @DisplayName("[SUCCESS]_[-]_[-]")
        public void success() {
            String universityCode = ""; String name = ""; String apiUrl = "";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);

            universityService.create(userToken, universityCode, name, apiUrl);

            verify(universityDao).addUniversityData(new UniversityDto(universityCode, name, apiUrl));
        }

        @Test
        @DisplayName("[ERROR]_[해당 code가 이미 존재할 시]_[ConflictException]")
        public void universityConflict_ConflictException() {
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.addUniversityData(new UniversityDto(any(), any(), any()))).thenThrow(ConflictException.class);

            Assertions.assertThrows(ConflictException.class, () -> universityService.create(userToken, any(), any(), any()));
        }

        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void isUnauthorizedToken_UnauthorizedException() {
            String universityCode = ""; String name = ""; String apiUrl = "";
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> universityService.create(userToken, universityCode, name, apiUrl)
            );
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void accessDenied_ForbiddenException() {
            String universityCode = ""; String name = ""; String apiUrl = "";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.masterOfDepartment1);

            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> universityService.create(userToken, universityCode, name, apiUrl)
            );
        }
    }

    @Nested
    @DisplayName("update()")
    public class TestUpdate {
        @Test
        @DisplayName("[SUCCESS]_[모든 멤버 변경 시]_[-]")
        public void success() {
            UniversityDto target = StubHelper.basicUniversity1;
            String targetCode = target.code();
            String newUniversityCode = ""; String newName = ""; String newApiUrl = "";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.getUniversityByCodeData(targetCode)).thenReturn(target);

            universityService.update(userToken, targetCode, newUniversityCode, newName, newApiUrl);

            verify(universityDao).updateUniversityData(targetCode, new UniversityDto(newUniversityCode, newName, newApiUrl));
        }

        @Test
        @DisplayName("[SUCCESS]_[newUniversityCode, newName이 null일 때]_[-]")
        public void success_newUniversityCodeIsNull() {
            UniversityDto target = StubHelper.basicUniversity1;
            String newApiUrl = any();
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.getUniversityByCodeData(target.code())).thenReturn(target);

            universityService.update(userToken, target.code(), null, null, newApiUrl);

            verify(universityDao).updateUniversityData(target.code(), new UniversityDto(target.code(), target.name(), newApiUrl));
        }

        @Test
        @DisplayName("[SUCCESS]_[newApiUrl이 null일 때]_[-]")
        public void success_newApiUrlIsNull() {
            UniversityDto target = StubHelper.basicUniversity1;
            String newUniversityCode = any(); String newName = any();
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.getUniversityByCodeData(target.code())).thenReturn(target);

            universityService.update(userToken, target.code(), newUniversityCode, newName, null);

            verify(universityDao).updateUniversityData(target.code(), new UniversityDto(newUniversityCode, newName, target.apiUrl()));
        }

        @Test
        @DisplayName("[SUCCESS]_[모든 member가 null일 때]_[-]")
        public void success_allNewMemberIsNull() {
            UniversityDto target = StubHelper.basicUniversity1;
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.getUniversityByCodeData(target.code())).thenReturn(target);

            universityService.update(userToken, target.code(), null, null, null);

            verify(universityDao, never()).updateUniversityData(target.code(), any());
        }

        @Test
        @DisplayName("[ERROR]_[target이 존재하지 않을 시]_[NotFoundException]")
        public void universityNotFound_NotFoundException() {
            String targetUniversityCode = "";
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.updateUniversityData(targetUniversityCode, any())).thenThrow(NotFoundException.class);

            Assertions.assertThrows(NotFoundException.class, () -> universityService.update(userToken, targetUniversityCode, any(), any(), any()));
        }

        @Test
        @DisplayName("[ERROR]_[해당 code가 이미 존재할 시]_[ConflictException]")
        public void universityConflict_ConflictException() {
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.developer);
            when(universityDao.updateUniversityData(any(), any())).thenThrow(ConflictException.class);

            Assertions.assertThrows(ConflictException.class, () -> universityService.update(userToken, any(), any(), any(), any()));
        }


        @Test
        @DisplayName("[ERROR]_[토큰이 검증되지 않을 시]_[UnauthorizedException]")
        public void isUnauthorizedToken_UnauthorizedException() {
            when(userDao.getByToken(userToken)).thenThrow(NotFoundException.class);

            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> universityService.update(userToken, any(), any(), any(), any())
            );
        }

        @Test
        @DisplayName("[ERROR]_[권한이 없을 시]_[ForbiddenException]")
        public void accessDenied_ForbiddenException() {
            when(userDao.getByToken(userToken)).thenReturn(StubHelper.masterOfDepartment1);

            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> universityService.update(userToken, any(), any(), any(), any())
            );
        }
    }
}
