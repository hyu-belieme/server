package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.MajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.model.dao.DepartmentDao;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDaoImpl extends BaseDaoImpl implements DepartmentDao {

    public DepartmentDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<DepartmentDto> getAllList() {
        List<DepartmentDto> output = new ArrayList<>();

        for (DepartmentEntity departmentEntity : departmentRepository.findAll()) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public List<DepartmentDto> getListByUniversity(String universityCode) {
        List<DepartmentDto> output = new ArrayList<>();

        int universityId = findUniversityEntity(universityCode).getId();
        for (DepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public DepartmentDto getByIndex(String universityCode, String departmentCode) {
        DepartmentEntity targetEntity = findDepartmentEntity(universityCode, departmentCode);
        return targetEntity.toDepartmentDto();
    }

    @Override
    public boolean checkExistByIndex(String universityCode, String departmentCode) {
        try {
            int universityId = findUniversityEntity(universityCode).getId();
            return departmentRepository.existsByUniversityIdAndCode(universityId, departmentCode);
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public DepartmentDto create(DepartmentDto newDepartment) {
        DepartmentEntity newDepartmentEntity = saveDepartmentOnly(newDepartment);
        saveBaseMajorJoins(newDepartmentEntity, newDepartment.baseMajors());

        return newDepartmentEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto update(String universityCode, String departmentCode, DepartmentDto newDepartment) {
        DepartmentEntity target = findDepartmentEntity(universityCode, departmentCode);
        updateDepartmentOnly(target, newDepartment);

        removeAllBaseMajorJoins(target);
        saveBaseMajorJoins(target, newDepartment.baseMajors());

        return target.toDepartmentDto();
    }

    private DepartmentEntity saveDepartmentOnly(DepartmentDto newDepartment) {
        UniversityEntity university = findUniversityEntity(newDepartment.university());

        checkDepartmentConflict(university.getId(), newDepartment.code());

        DepartmentEntity newDepartmentEntity = new DepartmentEntity(
                university,
                newDepartment.code(),
                newDepartment.name()
        );

        return departmentRepository.save(newDepartmentEntity);
    }

    private void updateDepartmentOnly(DepartmentEntity target, DepartmentDto newDepartment) {
        String newUniversityCode = newDepartment.university().code();
        UniversityEntity newUniversityEntity = findUniversityEntity(newUniversityCode);

        if (doesIndexOfDepartmentChange(target, newDepartment)) {
            checkDepartmentConflict(newUniversityEntity.getId(), newDepartment.code());
        }

        target.setCode(newDepartment.code())
                .setName(newDepartment.name())
                .setUniversity(newUniversityEntity);
    }

    private void saveBaseMajorJoins(DepartmentEntity newDepartmentEntity, List<MajorDto> baseMajors) {
        for (MajorDto baseMajor : baseMajors) {
            MajorEntity baseMajorEntity = findMajorEntity(baseMajor);
            MajorDepartmentJoinEntity newJoin = new MajorDepartmentJoinEntity(
                    baseMajorEntity,
                    newDepartmentEntity
            );
            majorDepartmentJoinRepository.save(newJoin);
        }
    }

    private void removeAllBaseMajorJoins(DepartmentEntity department) {
        while (department.getBaseMajorJoin().size() > 0) {
            majorDepartmentJoinRepository.delete(department.getBaseMajorJoin().get(0));
        }
    }

    private boolean doesIndexOfDepartmentChange(DepartmentEntity target, DepartmentDto newDepartment) {
        String oldUniversityCode = target.getUniversity().getCode();
        String oldDepartmentCode = target.getCode();
        return !(oldUniversityCode.equals(newDepartment.university().code())
                && oldDepartmentCode.equals(newDepartment.code()));
    }

    private void checkDepartmentConflict(int universityId, String departmentCode) {
        if (departmentRepository.existsByUniversityIdAndCode(universityId, departmentCode)) {
            throw new ConflictException();
        }
    }
}
