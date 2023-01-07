package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dto.*;
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
    protected final FakeDao<UserDto> userFakeDao = new FakeDao<>(DummyDataSet.userDummies);
    protected final FakeDao<AuthorityDto> authorityFakeDao = new FakeDao<>(DummyDataSet.authorityDummies);
    protected final FakeDao<StuffDto> stuffFakeDao = new FakeDao<>(DummyDataSet.stuffDummies);
    protected final FakeDao<ItemDto> itemFakeDao = new FakeDao<>(DummyDataSet.itemDummies);
    protected final FakeDao<HistoryDto> historyFakeDao = new FakeDao<>(DummyDataSet.historyDummies);

    protected UniversityDto getUniversityDummy(String universityCode) {
        FakeDao.FilterMethod<UniversityDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(UniversityDto target) {
                return universityCode.equals(target.getCode());
            }
        };

        return universityFakeDao.getFirstByCondition(compareRoutine);
    }

    protected DepartmentDto getDepartmentDummy(String universityCode, String departmentCode) {
        FakeDao.FilterMethod<DepartmentDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(DepartmentDto target) {
                return universityCode.equals(target.getUniversity().getCode())
                        && departmentCode.equals(target.getCode());
            }
        };

        return departmentFakeDao.getFirstByCondition(compareRoutine);
    }

    protected MajorDto getMajorDummy(String universityCode, String majorCode) {
        FakeDao.FilterMethod<MajorDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(MajorDto target) {
                return universityCode.equals(target.getUniversity().getCode())
                        && majorCode.equals(target.getCode());
            }
        };

        return majorFakeDao.getFirstByCondition(compareRoutine);
    }

    protected UserDto getUserDummy(String universityCode, String studentId) {
        FakeDao.FilterMethod<UserDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(UserDto target) {
                return universityCode.equals(target.getUniversity().getCode())
                        && studentId.equals(target.getStudentId());
            }
        };

        return userFakeDao.getFirstByCondition(compareRoutine);
    }

    protected AuthorityDto getAuthorityDummy(
            String universityCodeForUser, String studentId,
            String universityCodeForDepartment, String departmentCode) {
        FakeDao.FilterMethod<AuthorityDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(AuthorityDto target) {
                return universityCodeForUser.equals(target.getUser().getUniversity().getCode())
                        && studentId.equals(target.getUser().getStudentId())
                        && universityCodeForDepartment.equals(target.getDepartment().getUniversity().getCode())
                        && departmentCode.equals(target.getDepartment().getCode());
            }
        };

        return authorityFakeDao.getFirstByCondition(compareRoutine);
    }

    protected StuffDto getStuffDummy(
            String universityCode, String departmentCode, String stuffName) {
        FakeDao.FilterMethod<StuffDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(StuffDto target) {
                return universityCode.equals(target.getDepartment().getUniversity().getCode())
                        && departmentCode.equals(target.getDepartment().getCode())
                        && stuffName.equals(target.getName());
            }
        };

        return stuffFakeDao.getFirstByCondition(compareRoutine);
    }

    protected ItemDto getItemDummy(
            String universityCode, String departmentCode,
            String stuffName, int itemNum) {
        FakeDao.FilterMethod<ItemDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(ItemDto target) {
                return universityCode.equals(target.getStuff().getDepartment().getUniversity().getCode())
                        && departmentCode.equals(target.getStuff().getDepartment().getCode())
                        && stuffName.equals(target.getStuff().getName())
                        && itemNum == target.getNum();
            }
        };

        return itemFakeDao.getFirstByCondition(compareRoutine);
    }

    protected HistoryDto getHistoryDummy(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, int historyNum) {
        FakeDao.FilterMethod<HistoryDto> compareRoutine = new FakeDao.FilterMethod<>() {
            @Override
            public boolean checkCondition(HistoryDto target) {
                return universityCode.equals(target.getItem().getStuff().getDepartment().getUniversity().getCode())
                        && departmentCode.equals(target.getItem().getStuff().getDepartment().getCode())
                        && stuffName.equals(target.getItem().getStuff().getName())
                        && itemNum == target.getItem().getNum()
                        && historyNum == target.getNum();
            }
        };

        return historyFakeDao.getFirstByCondition(compareRoutine);
    }
}