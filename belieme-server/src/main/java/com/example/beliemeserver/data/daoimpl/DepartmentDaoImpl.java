package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.MajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.DepartmentDao;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDaoImpl extends BaseDaoImpl implements DepartmentDao {

    public DepartmentDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<DepartmentDto> getAllDepartmentsData() {
        List<DepartmentDto> output = new ArrayList<>();

        for(DepartmentEntity departmentEntity : departmentRepository.findAll()) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public List<DepartmentDto> getAllDepartmentsByUniversityCodeData(String universityCode) throws NotFoundException {
        List<DepartmentDto> output = new ArrayList<>();

        int universityId = findUniversityEntity(universityCode).getId();
        for(DepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public DepartmentDto getDepartmentByUniversityCodeAndDepartmentCodeData(String universityCode, String departmentCode) throws NotFoundException {
        DepartmentEntity targetEntity = findDepartmentEntity(universityCode, departmentCode);
        return targetEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto addDepartmentData(DepartmentDto newDepartment) throws NotFoundException, ConflictException {
        DepartmentEntity newDepartmentEntity = saveDepartmentOnly(newDepartment);
        saveBaseMajorJoins(newDepartmentEntity, newDepartment.baseMajors());

        return newDepartmentEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto updateDepartmentData(String universityCode, String departmentCode, DepartmentDto newDepartment) throws NotFoundException, ConflictException {
        DepartmentEntity target = findDepartmentEntity(universityCode, departmentCode);
        updateDepartmentOnly(target, newDepartment);

        removeAllBaseMajorJoins(target);
        saveBaseMajorJoins(target, newDepartment.baseMajors());

        return target.toDepartmentDto();
    }

    @Override
    public DepartmentDto putBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto newBaseMajor) throws NotFoundException, ConflictException {
        DepartmentEntity targetEntity = findDepartmentEntity(universityCode, departmentCode);
        MajorEntity newMajorEntity = findMajorEntity(newBaseMajor);

        checkBaseMajorConflict(targetEntity, newMajorEntity);

        MajorDepartmentJoinEntity newJoin = new MajorDepartmentJoinEntity(
                newMajorEntity,
                targetEntity
        );
        majorDepartmentJoinRepository.save(newJoin);

        return targetEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto removeBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto targetBaseMajor) throws NotFoundException {
        DepartmentEntity targetEntity = findDepartmentEntity(universityCode, departmentCode);
        MajorEntity targetBaseMajorEntity = findMajorEntity(targetBaseMajor);

        for(MajorDepartmentJoinEntity majorDepartmentJoin : targetEntity.getBaseMajorJoin()) {
            if(majorDepartmentJoin.getMajorId() == targetBaseMajorEntity.getId()) {
                majorDepartmentJoinRepository.delete(majorDepartmentJoin);
                break;
            }
        }

        return targetEntity.toDepartmentDto();
    }

    private DepartmentEntity saveDepartmentOnly(DepartmentDto newDepartment) throws NotFoundException, ConflictException {
        UniversityEntity university = findUniversityEntity(newDepartment.university());

        checkDepartmentConflict(university.getId(), newDepartment.code());

        DepartmentEntity newDepartmentEntity = new DepartmentEntity(
                university,
                newDepartment.code(),
                newDepartment.name()
        );

        return departmentRepository.save(newDepartmentEntity);
    }

    private void updateDepartmentOnly(DepartmentEntity target, DepartmentDto newDepartment) throws NotFoundException, ConflictException {
        String newUniversityCode = newDepartment.university().code();
        UniversityEntity newUniversityEntity = findUniversityEntity(newUniversityCode);

        if(doesIndexOfDepartmentChange(target, newDepartment)) {
            checkDepartmentConflict(newUniversityEntity.getId(), newDepartment.code());
        }

        target.setCode(newDepartment.code())
                .setName(newDepartment.name())
                .setUniversity(newUniversityEntity);
    }

    private void saveBaseMajorJoins(DepartmentEntity newDepartmentEntity, List<MajorDto> baseMajors) throws NotFoundException {
        for(MajorDto baseMajor: baseMajors) {
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

    private void checkDepartmentConflict(int universityId, String departmentCode) throws ConflictException {
        if(departmentRepository.existsByUniversityIdAndCode(universityId, departmentCode)) {
            throw new ConflictException();
        }
    }

    private void checkBaseMajorConflict(DepartmentEntity targetDepartment, MajorEntity newMajor) throws ConflictException {
        for(MajorDepartmentJoinEntity majorDepartmentJoin : targetDepartment.getBaseMajorJoin()) {
            if(majorDepartmentJoin.getMajorId() == newMajor.getId()) {
                throw new ConflictException();
            }
        }
    }
}
