package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MajorDaoTest extends DaoTest {
    @Autowired
    private MajorDao majorDao;

    private final UniversityDto hyu = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/");
    private final UniversityDto cku = new UniversityDto("CKU", "가톨릭관동대학교", null);

    private final List<MajorDto> initialData = List.of(
            new MajorDto(hyu, "FH04067"),
            new MajorDto(hyu, "FH04068"),
            new MajorDto(hyu, "FH04069"),
            new MajorDto(cku, "TEST"),
            new MajorDto(hyu, "TEST"),
            new MajorDto(cku, "A68"),
            new MajorDto(cku, "A69"),
            new MajorDto(cku, "A70")
    );

    @Test
    public void getAllMajorsTest() throws DataException {
        List<MajorDto> result = majorDao.getAllMajorsData();
        assertThatAllElementIsEqual(initialData, result);
    }

    @Test
    public void createNewMajorTest1() throws DataException, ConflictException, NotFoundException {
        MajorDto newMajor = new MajorDto(hyu, "FH04070");

        MajorDto result = majorDao.addMajorData(newMajor);
        Assertions.assertThat(result).isEqualTo(newMajor);

        List<MajorDto> expectedDBStatus = new ArrayList<>(initialData);
        expectedDBStatus.add(newMajor);

        List<MajorDto> resultDBStatus = majorDao.getAllMajorsData();
        assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
    }

    @Test
    public void createNewMajorTest2() throws DataException, ConflictException, NotFoundException {
        MajorDto newMajor = new MajorDto(cku, "FH04068");

        MajorDto result = majorDao.addMajorData(newMajor);
        Assertions.assertThat(result).isEqualTo(newMajor);

        List<MajorDto> expectedDBStatus = new ArrayList<>(initialData);
        expectedDBStatus.add(newMajor);

        List<MajorDto> resultDBStatus = majorDao.getAllMajorsData();
        assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
    }

    @Test
    public void createNewMajorFailByConflictExceptionTest() {
        MajorDto newMajor = new MajorDto(hyu, "FH04068");

        Assertions.assertThatThrownBy(() -> majorDao.addMajorData(newMajor))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void createNewMajorFailByNotFoundExceptionTest() {
        UniversityDto notFoundedUniversity = new UniversityDto("HANYANG", "한양대학교", null);
        MajorDto newMajor = new MajorDto(notFoundedUniversity, "FH04070");

        Assertions.assertThatThrownBy(() -> majorDao.addMajorData(newMajor))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updateNewMajorTest1() throws DataException, ConflictException, NotFoundException {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(hyu, "FH04070");

        MajorDto result = majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor);
        Assertions.assertThat(result).isEqualTo(newMajor);

        List<MajorDto> expectedDBStatus = new ArrayList<>(initialData);
        expectedDBStatus.removeIf(
                majorDto -> (majorDto.getCode().equals(targetMajorCode)
                                && majorDto.getUniversity().getCode().equals(targetUniversityCode))
        );
        expectedDBStatus.add(newMajor);

        List<MajorDto> resultDBStatus = majorDao.getAllMajorsData();
        assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
    }

    @Test
    public void updateNewMajorTest2() throws DataException, ConflictException, NotFoundException {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(cku, "FH04069");

        MajorDto result = majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor);
        Assertions.assertThat(result).isEqualTo(newMajor);

        List<MajorDto> expectedDBStatus = new ArrayList<>(initialData);
        expectedDBStatus.removeIf(
                majorDto -> (majorDto.getCode().equals(targetMajorCode)
                        && majorDto.getUniversity().getCode().equals(targetUniversityCode))
        );
        expectedDBStatus.add(newMajor);

        List<MajorDto> resultDBStatus = majorDao.getAllMajorsData();
        assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
    }

    @Test
    public void updateNewMajorTest3() throws DataException, ConflictException, NotFoundException {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(cku, "FH04068");

        MajorDto result = majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor);
        Assertions.assertThat(result).isEqualTo(newMajor);

        List<MajorDto> expectedDBStatus = new ArrayList<>(initialData);
        expectedDBStatus.removeIf(
                majorDto -> (majorDto.getCode().equals(targetMajorCode)
                        && majorDto.getUniversity().getCode().equals(targetUniversityCode))
        );
        expectedDBStatus.add(newMajor);

        List<MajorDto> resultDBStatus = majorDao.getAllMajorsData();
        assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
    }

    @Test
    public void updateNewMajorFailByConflictExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04069";
        MajorDto newMajor = new MajorDto(hyu, "FH04068");

        Assertions.assertThatThrownBy(() -> majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void updateNewMajorFailByNotFoundExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetMajorCode = "FH04072";
        MajorDto newMajor = new MajorDto(hyu, "FH04080");

        Assertions.assertThatThrownBy(() -> majorDao.updateMajorData(targetUniversityCode, targetMajorCode, newMajor))
                .isInstanceOf(NotFoundException.class);
    }

    private void assertThatAllElementIsEqual(List<MajorDto> expected, List<MajorDto> result) {
        Assertions.assertThat(result.size()).isEqualTo(expected.size());

        for(MajorDto majorDto : result) {
            Assertions.assertThat(expected).contains(majorDto);
        }
    }
}
