package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dao.DepartmentDao;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoTest extends DaoTest {
    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void getAllDepartmentsTest() {
        List<DepartmentDto> result;
        try {
            result = departmentDao.getAllDepartmentsData();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }
        assertThatAllElementIsEqual(result, InitialData.departmentDummies);
    }

    @Test
    public void getAllDepartmentsByUniversityCodeTest() {
        String targetUniversityCode = "HYU";

        List<DepartmentDto> result;
        try {
            result = departmentDao.getAllDepartmentsByUniversityCodeData(targetUniversityCode);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        List<DepartmentDto> expected = new ArrayList<>();
        for(DepartmentDto departmentDto : InitialData.departmentDummies) {
            String universityCode = departmentDto.getUniversity().getCode();
            if(targetUniversityCode.equals(universityCode)) {
                expected.add(departmentDto);
            }
        }

        assertThatAllElementIsEqual(result, expected);
    }

    @Test
    public void getAllDepartmentsByUniversityCodeFailByNotFoundExceptionTest() {
        String targetUniversityCode = "KNU";

        Assertions.assertThatThrownBy(() -> departmentDao.getAllDepartmentsByUniversityCodeData(targetUniversityCode))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getDepartmentByUniversityCodeAndDepartmentCodeTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        DepartmentDto result;
        try {
            result = departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
            return;
        }

        DepartmentDto expected = null;
        for(DepartmentDto departmentDto : InitialData.departmentDummies) {
            if(targetUniversityCode.equals(departmentDto.getUniversity().getCode())
                    && targetDepartmentCode.equals(departmentDto.getCode())) {
                expected = departmentDto;
            }
        }
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getDepartmentByUniversityCodeAndDepartmentCodeFailByNotFoundExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "MED";

        Assertions.assertThatThrownBy(() -> departmentDao.getDepartmentByUniversityCodeAndDepartmentCodeData(targetUniversityCode, targetDepartmentCode))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void createNewDepartmentTest1() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(0),
                InitialData.majorDummies.get(1),
                InitialData.majorDummies.get(2)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(0),
                "ENG",
                "공과대학",
                newDepartmentBaseMajor
        );

        try {
            DepartmentDto result = departmentDao.addDepartmentData(newDepartment);
            Assertions.assertThat(result).isEqualTo(newDepartment);

            List<DepartmentDto> expectedDBStatus = new ArrayList<>(InitialData.departmentDummies);
            expectedDBStatus.add(newDepartment);

            List<DepartmentDto> resultDBStatus = departmentDao.getAllDepartmentsData();
            assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void createNewDepartmentTest2() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(1),
                "CSE",
                "컴퓨터공학과",
                newDepartmentBaseMajor
        );

        try {
            DepartmentDto result = departmentDao.addDepartmentData(newDepartment);
            Assertions.assertThat(result).isEqualTo(newDepartment);

            List<DepartmentDto> expectedDBStatus = new ArrayList<>(InitialData.departmentDummies);
            expectedDBStatus.add(newDepartment);

            List<DepartmentDto> resultDBStatus = departmentDao.getAllDepartmentsData();
            assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void createNewDepartmentFailByConflictExceptionTest() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(1),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        Assertions.assertThatThrownBy(() -> departmentDao.addDepartmentData(newDepartment))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void createNewDepartmentFailByNotFoundExceptionTest() {
        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                SampleData.notFoundedUniversity,
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        Assertions.assertThatThrownBy(() -> departmentDao.addDepartmentData(newDepartment))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updateNewDepartmentTest1() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(0),
                InitialData.majorDummies.get(1)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(0),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        try {
            DepartmentDto result = departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment);
            Assertions.assertThat(result).isEqualTo(newDepartment);

            List<DepartmentDto> expectedDBStatus = new ArrayList<>(InitialData.departmentDummies);
            expectedDBStatus.removeIf(
                    departmentDto -> (targetUniversityCode.equals(departmentDto.getUniversity().getCode())
                            && targetDepartmentCode.equals(departmentDto.getCode()))
            );
            expectedDBStatus.add(newDepartment);

            List<DepartmentDto> resultDBStatus = departmentDao.getAllDepartmentsData();
            assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void updateNewDepartmentTest2() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(0),
                InitialData.majorDummies.get(1),
                InitialData.majorDummies.get(2)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(0),
                "ENG",
                "공과대학",
                newDepartmentBaseMajor
        );

        try {
            DepartmentDto result = departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment);
            Assertions.assertThat(result).isEqualTo(newDepartment);

            List<DepartmentDto> expectedDBStatus = new ArrayList<>(InitialData.departmentDummies);
            expectedDBStatus.removeIf(
                    departmentDto -> (targetUniversityCode.equals(departmentDto.getUniversity().getCode())
                            && targetDepartmentCode.equals(departmentDto.getCode()))
            );
            expectedDBStatus.add(newDepartment);

            List<DepartmentDto> resultDBStatus = departmentDao.getAllDepartmentsData();
            assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void updateNewDepartmentFailByNotFoundExceptionTest1() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "COMPUTER";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(0),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        Assertions.assertThatThrownBy(() -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updateNewDepartmentFailByNotFoundExceptionTest2() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(6),
                SampleData.notFoundedMajor
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(0),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        Assertions.assertThatThrownBy(() -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void updateNewDepartmentFailByConflictExceptionTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        List<MajorDto> newDepartmentBaseMajor = List.of(
                InitialData.majorDummies.get(6)
        );

        DepartmentDto newDepartment = new DepartmentDto(
                InitialData.universityDummies.get(1),
                "MED",
                "의과대학",
                newDepartmentBaseMajor
        );

        Assertions.assertThatThrownBy(() -> departmentDao.updateDepartmentData(targetUniversityCode, targetDepartmentCode, newDepartment))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    public void putBaseMajorOnDepartmentTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        String newUniversityCode = "HYU";
        String newMajorCode = "FH04069";

        MajorDto newMajor = InitialData.getMajorDummy(newUniversityCode, newMajorCode);

        DepartmentDto targetDepartment = InitialData.getDepartmentDummy(targetUniversityCode, targetDepartmentCode);
        List<MajorDto> expectedBaseMajors = new ArrayList<>(targetDepartment.getBaseMajors());
        expectedBaseMajors.add(newMajor);

        DepartmentDto expectedResult = new DepartmentDto(
                InitialData.getUniversityDummy(targetUniversityCode),
                targetDepartmentCode,
                targetDepartment.getName(),
                expectedBaseMajors
        );

        try {
            DepartmentDto result = departmentDao.putBaseMajorOnDepartmentData(targetUniversityCode, targetDepartmentCode, newMajor);
            Assertions.assertThat(result).isEqualTo(expectedResult);

            List<DepartmentDto> expectedDBStatus = new ArrayList<>(InitialData.departmentDummies);
            expectedDBStatus.removeIf(
                    departmentDto -> (targetUniversityCode.equals(departmentDto.getUniversity().getCode())
                            && targetDepartmentCode.equals(departmentDto.getCode()))
            );
            expectedDBStatus.add(expectedResult);

            List<DepartmentDto> resultDBStatus = departmentDao.getAllDepartmentsData();
            assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void removeBaseMajorOnDepartmentTest() {
        String targetUniversityCode = "HYU";
        String targetDepartmentCode = "CSE";

        String targetMajorUniversityCode = "HYU";
        String targetMajorCode = "FH04068";

        MajorDto targetMajor = InitialData.getMajorDummy(targetMajorUniversityCode, targetMajorCode);

        DepartmentDto targetDepartment = InitialData.getDepartmentDummy(targetUniversityCode, targetDepartmentCode);
        List<MajorDto> expectedBaseMajors = new ArrayList<>(targetDepartment.getBaseMajors());
        expectedBaseMajors.remove(targetMajor);

        DepartmentDto expectedResult = new DepartmentDto(
                InitialData.getUniversityDummy(targetUniversityCode),
                targetDepartmentCode,
                targetDepartment.getName(),
                expectedBaseMajors
        );

        try {
            DepartmentDto result = departmentDao.removeBaseMajorOnDepartmentData(targetUniversityCode, targetDepartmentCode, targetMajor);
            Assertions.assertThat(result).isEqualTo(expectedResult);

            List<DepartmentDto> expectedDBStatus = new ArrayList<>(InitialData.departmentDummies);
            expectedDBStatus.removeIf(
                    departmentDto -> (targetUniversityCode.equals(departmentDto.getUniversity().getCode())
                            && targetDepartmentCode.equals(departmentDto.getCode()))
            );
            expectedDBStatus.add(expectedResult);

            List<DepartmentDto> resultDBStatus = departmentDao.getAllDepartmentsData();
            assertThatAllElementIsEqual(expectedDBStatus, resultDBStatus);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    private void assertThatAllElementIsEqual(List<DepartmentDto> expected, List<DepartmentDto> result) {
        Assertions.assertThat(result.size()).isEqualTo(expected.size());

        for(DepartmentDto departmentDto : result) {
            Assertions.assertThat(expected).contains(departmentDto);
        }
    }
}
