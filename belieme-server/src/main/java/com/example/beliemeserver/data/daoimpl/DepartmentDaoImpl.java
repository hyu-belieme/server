package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.MajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.DepartmentRepository;
import com.example.beliemeserver.data.repository.MajorDepartmentJoinRepository;
import com.example.beliemeserver.data.repository.MajorRepository;
import com.example.beliemeserver.data.repository.UniversityRepository;
import com.example.beliemeserver.model.dao.DepartmentDao;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentDaoImpl implements DepartmentDao {
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorDepartmentJoinRepository majorDepartmentJoinRepository;

    public DepartmentDaoImpl(UniversityRepository universityRepository, MajorRepository majorRepository, DepartmentRepository departmentRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository) {
        this.universityRepository = universityRepository;
        this.majorRepository = majorRepository;
        this.departmentRepository = departmentRepository;
        this.majorDepartmentJoinRepository = majorDepartmentJoinRepository;
    }

    @Override
    public List<DepartmentDto> getAllDepartmentsData() throws DataException {
        List<DepartmentDto> output = new ArrayList<>();

        for(DepartmentEntity departmentEntity : departmentRepository.findAll()) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public List<DepartmentDto> getAllDepartmentsByUniversityCodeData(String universityCode) throws DataException, NotFoundException {
        List<DepartmentDto> output = new ArrayList<>();

        int universityId = getUniversityEntity(universityCode).getId();
        for(DepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public DepartmentDto getDepartmentByUniversityCodeAndDepartmentCodeData(String universityCode, String departmentCode) throws DataException, NotFoundException {
        DepartmentEntity targetEntity = getDepartmentEntity(universityCode, departmentCode);
        return targetEntity.toDepartmentDto();
    }

    @Override
    @Transactional
    public DepartmentDto addDepartmentData(DepartmentDto newDepartment) throws DataException, NotFoundException, ConflictException {
        DepartmentEntity newDepartmentEntity = saveDepartmentOnly(newDepartment);
        saveBaseMajorJoins(newDepartmentEntity, newDepartment.getBaseMajors());

        return newDepartmentEntity.toDepartmentDto();
    }

    @Override
    @Transactional
    public DepartmentDto updateDepartmentData(String universityCode, String departmentCode, DepartmentDto newDepartment) throws DataException, NotFoundException, ConflictException {
        DepartmentEntity target = getDepartmentEntity(universityCode, departmentCode);
        DepartmentEntity newDepartmentEntity = updateDepartmentOnly(target, newDepartment);

        removeAllBaseMajorJoins(newDepartmentEntity);
        saveBaseMajorJoins(newDepartmentEntity, newDepartment.getBaseMajors());

        return newDepartmentEntity.toDepartmentDto();
    }

    @Override
    @Transactional
    public DepartmentDto putBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto newBaseMajor) throws NotFoundException, ConflictException {
        DepartmentEntity targetEntity = getDepartmentEntity(universityCode, departmentCode);
        MajorEntity newMajorEntity = getMajorEntityFromDto(newBaseMajor);

        checkBaseMajorConflict(targetEntity, newMajorEntity);

        MajorDepartmentJoinEntity newJoin = new MajorDepartmentJoinEntity(
                newMajorEntity.getId(),
                targetEntity.getId(),
                newMajorEntity,
                targetEntity
        );
        majorDepartmentJoinRepository.save(newJoin);

        return targetEntity.toDepartmentDto();
    }

    @Override
    @Transactional
    public DepartmentDto removeBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto targetBaseMajor) throws NotFoundException {
        DepartmentEntity targetEntity = getDepartmentEntity(universityCode, departmentCode);
        MajorEntity targetBaseMajorEntity = getMajorEntityFromDto(targetBaseMajor);

        for(MajorDepartmentJoinEntity majorDepartmentJoin : targetEntity.getBaseMajorJoin()) {
            if(majorDepartmentJoin.getMajorId() == targetBaseMajorEntity.getId()) {
                majorDepartmentJoinRepository.delete(majorDepartmentJoin);
                break;
            }
        }

        return targetEntity.toDepartmentDto();
    }

    private UniversityEntity getUniversityEntity(String universityCode) throws NotFoundException {
        return IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode);
    }

    private DepartmentEntity getDepartmentEntity(String universityCode, String departmentCode) throws NotFoundException {
        int universityId = getUniversityEntity(universityCode).getId();

        return IndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentCode);
    }

    private MajorEntity getMajorEntityFromDto(MajorDto majorDto) throws NotFoundException {
        String universityCode = majorDto.getUniversity().getCode();
        String majorCode = majorDto.getCode();
        int universityId = getUniversityEntity(universityCode).getId();

        return IndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
    }

    private DepartmentEntity saveDepartmentOnly(DepartmentDto newDepartment) throws NotFoundException, ConflictException {
        String universityCode = newDepartment.getUniversity().getCode();
        String departmentCode = newDepartment.getCode();

        UniversityEntity university = getUniversityEntity(universityCode);
        checkDepartmentConflict(university.getId(), departmentCode);

        DepartmentEntity newDepartmentEntity = new DepartmentEntity(
                university.getId(),
                departmentCode,
                newDepartment.getName(),
                university
        );
        return departmentRepository.save(newDepartmentEntity);
    }

    private DepartmentEntity updateDepartmentOnly(DepartmentEntity target, DepartmentDto newDepartment) throws NotFoundException, ConflictException {
        String targetUniversityCode = target.getUniversity().getCode();
        String targetDepartmentCode = target.getCode();

        String newUniversityCode = newDepartment.getUniversity().getCode();
        UniversityEntity newUniversityEntity = getUniversityEntity(newUniversityCode);

        if(doesIndexOfDepartmentChange(targetUniversityCode, targetDepartmentCode, newDepartment)) {
            checkDepartmentConflict(newUniversityEntity.getId(), newDepartment.getCode());
        }

        DepartmentEntity newDepartmentEntity = new DepartmentEntity(
                target.getId(),
                newUniversityEntity.getId(),
                newDepartment.getCode(),
                newDepartment.getName(),
                newUniversityEntity,
                target.getBaseMajorJoin()
        );
        departmentRepository.save(newDepartmentEntity);
        return newDepartmentEntity;
    }

    private void saveBaseMajorJoins(DepartmentEntity newDepartmentEntity, List<MajorDto> baseMajors) throws NotFoundException {
        for(MajorDto baseMajor: baseMajors) {
            MajorEntity baseMajorEntity = getMajorEntityFromDto(baseMajor);
            MajorDepartmentJoinEntity newJoin = new MajorDepartmentJoinEntity(
                    baseMajorEntity.getId(),
                    newDepartmentEntity.getId(),
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

    private boolean doesIndexOfDepartmentChange(String targetUniversityCode, String targetDepartmentCode, DepartmentDto newDepartment) {
        String newUniversityCode = newDepartment.getUniversity().getCode();
        String newDepartmentCode = newDepartment.getCode();
        return !(targetUniversityCode.equals(newUniversityCode) && targetDepartmentCode.equals(newDepartmentCode));
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
