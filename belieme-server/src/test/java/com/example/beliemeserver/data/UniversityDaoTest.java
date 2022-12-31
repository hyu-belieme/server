package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.UniversityDao;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UniversityDaoTest {
    @Autowired
    private UniversityDao universityDao;

    @Test
    public void getAllUniversitiesTest() throws DataException {
        List<UniversityDto> expected = List.of(
                new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/"),
                new UniversityDto("CKU", "가톨릭관동대학교", null),
                new UniversityDto("SNU", "서울대학교", null)
        );

        List<UniversityDto> result = universityDao.getAllUniversitiesData();
        assertThatAllElementIsEqual(expected, result);
    }

    @Test
    public void getUniversityByCodeTest() throws DataException, NotFoundException {
        UniversityDto expected = new UniversityDto("CKU", "가톨릭관동대학교", null);

        UniversityDto result = universityDao.getUniversityByCodeData("CKU");
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getUniversityByCodeFailTest() {
        Assertions.assertThatThrownBy(() -> universityDao.getUniversityByCodeData("HANYANG"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void createNewUniversityTest() throws DataException, ConflictException {
        UniversityDto newUniversity = new UniversityDto("KNU", "강원대학교", null);

        List<UniversityDto> expectedDBStatus = universityDao.getAllUniversitiesData();
        expectedDBStatus.add(newUniversity);

        UniversityDto result = universityDao.addUniversityData(newUniversity);
        Assertions.assertThat(result).isEqualTo(newUniversity);

        List<UniversityDto> resultDBStatus = universityDao.getAllUniversitiesData();
        Assertions.assertThat(resultDBStatus.size()).isEqualTo(expectedDBStatus.size());

        for(UniversityDto universityDto : resultDBStatus) {
            Assertions.assertThat(expectedDBStatus).contains(universityDto);
        }
    }

    @Test
    public void createNewUniversityFailTest() {
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        Assertions.assertThatThrownBy(() -> universityDao.addUniversityData(newUniversity))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void updateNewUniversityTest() throws DataException, NotFoundException, ConflictException {
        String targetUnivCode = "HYU";
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        List<UniversityDto> expectedDBStatus = universityDao.getAllUniversitiesData();

        expectedDBStatus.removeIf(universityDto -> universityDto.getCode().equals(targetUnivCode));
        expectedDBStatus.add(newUniversity);

        UniversityDto result = universityDao.updateUniversityData(targetUnivCode, newUniversity);
        Assertions.assertThat(result).isEqualTo(newUniversity);

        List<UniversityDto> resultDBStatus = universityDao.getAllUniversitiesData();
        Assertions.assertThat(resultDBStatus.size()).isEqualTo(expectedDBStatus.size());

        for(UniversityDto universityDto : resultDBStatus) {
            Assertions.assertThat(expectedDBStatus).contains(universityDto);
        }
    }

    @Test
    public void updateNewUniversityWithCodeChangeTest() throws DataException, NotFoundException, ConflictException {
        String targetUnivCode = "HYU";
        UniversityDto newUniversity = new UniversityDto("HYU-ERICA", "한양대학교 에리카", null);

        List<UniversityDto> expectedDBStatus = universityDao.getAllUniversitiesData();

        expectedDBStatus.removeIf(universityDto -> universityDto.getCode().equals(targetUnivCode));
        expectedDBStatus.add(newUniversity);

        UniversityDto result = universityDao.updateUniversityData(targetUnivCode, newUniversity);
        Assertions.assertThat(result).isEqualTo(newUniversity);

        List<UniversityDto> resultDBStatus = universityDao.getAllUniversitiesData();
        Assertions.assertThat(resultDBStatus.size()).isEqualTo(expectedDBStatus.size());

        for(UniversityDto universityDto : resultDBStatus) {
            Assertions.assertThat(expectedDBStatus).contains(universityDto);
        }
    }

    @Test
    public void updateNewUniversityFailByNotFoundExceptionTest() {
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        Assertions.assertThatThrownBy(() -> universityDao.updateUniversityData("HYU2", newUniversity))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updateNewUniversityFailByConflictExceptionTest() {
        UniversityDto newUniversity = new UniversityDto("HYU", "한양대학교", null);

        Assertions.assertThatThrownBy(() -> universityDao.updateUniversityData("SNU", newUniversity))
                .isInstanceOf(ConflictException.class);
    }

    private void assertThatAllElementIsEqual(List<UniversityDto> expected, List<UniversityDto> result) {
        Assertions.assertThat(result.size()).isEqualTo(expected.size());

        for(UniversityDto universityDto : result) {
            Assertions.assertThat(expected).contains(universityDto);
        }
    }
}
