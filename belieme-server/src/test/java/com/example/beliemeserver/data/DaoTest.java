package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.util.FakeDao;
import com.example.beliemeserver.util.DummyDataSet;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class DaoTest {
    @BeforeAll
    public static void initDummyDataSet() {
        DummyDataSet.init();
    }

    protected final FakeDao<UniversityDto> universityFakeDao = new FakeDao<>(DummyDataSet.universityDummies);
    protected final FakeDao<MajorDto> majorFakeDao = new FakeDao<>(DummyDataSet.majorDummies);
    protected final FakeDao<DepartmentDto> departmentFakeDao = new FakeDao<>(DummyDataSet.departmentDummies);

    protected UniversityDto getUniversityDummyByIndex(String universityCode) {
        FakeDao.FilterMethod<UniversityDto> compareRoutine = new FakeDao.FilterMethod<UniversityDto>() {
            @Override
            public boolean checkCondition(UniversityDto target) {
                return universityCode.equals(target.getCode());
            }
        };

        return universityFakeDao.getFirstByCondition(compareRoutine);
    }

    protected DepartmentDto getDepartmentDummyByIndex(String universityCode, String departmentCode) {
        FakeDao.FilterMethod<DepartmentDto> compareRoutine = new FakeDao.FilterMethod<DepartmentDto>() {
            @Override
            public boolean checkCondition(DepartmentDto target) {
                return universityCode.equals(target.getUniversity().getCode())
                        && departmentCode.equals(target.getCode());
            }
        };

        return departmentFakeDao.getFirstByCondition(compareRoutine);
    }

    protected MajorDto getMajorDummyByIndex(String universityCode, String majorCode) {
        FakeDao.FilterMethod<MajorDto> compareRoutine = new FakeDao.FilterMethod<MajorDto>() {
            @Override
            public boolean checkCondition(MajorDto target) {
                return universityCode.equals(target.getUniversity().getCode())
                        && majorCode.equals(target.getCode());
            }
        };

        return majorFakeDao.getFirstByCondition(compareRoutine);
    }
}