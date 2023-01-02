package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.MajorDepartmentJoinEntity;
import com.example.beliemeserver.data.entity.MajorEntity;
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

        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        for(DepartmentEntity departmentEntity : departmentRepository.findByUniversityId(universityId)) {
            output.add(departmentEntity.toDepartmentDto());
        }
        return output;
    }

    @Override
    public DepartmentDto getDepartmentByUniversityCodeAndDepartmentCodeData(String universityCode, String departmentCode) throws DataException, NotFoundException {
        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();

        DepartmentEntity targetEntity = IndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentCode);
        return targetEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto addDepartmentData(DepartmentDto newDepartment) throws DataException, NotFoundException, ConflictException {
        String universityCode = newDepartment.getUniversity().getCode();
        String departmentCode = newDepartment.getCode();

        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();

        if(departmentRepository.existsByUniversityIdAndCode(universityId, departmentCode)) {
            throw new ConflictException();
        }

        DepartmentEntity newDepartmentEntity = new DepartmentEntity(
                universityId,
                departmentCode,
                newDepartment.getName()
        );

        DepartmentEntity savedDepartmentEntity = departmentRepository.save(newDepartmentEntity);
        return savedDepartmentEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto updateDepartmentData(String universityCode, String departmentCode, DepartmentDto newDepartment) throws DataException, NotFoundException {
        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        int targetId = IndexAdapter.getDepartmentEntity(departmentRepository, universityId, departmentCode).getId();

        String newUniversityCode = newDepartment.getUniversity().getCode();
        int newUniversityId = IndexAdapter.getUniversityEntityByCode(universityRepository, newUniversityCode).getId();
        DepartmentEntity newDepartmentEntity = new DepartmentEntity(
                targetId,
                newUniversityId,
                newDepartment.getCode(),
                newDepartment.getName()
        );

        DepartmentEntity savedDepartmentEntity = departmentRepository.save(newDepartmentEntity);
        return savedDepartmentEntity.toDepartmentDto();
    }

    @Override
    public DepartmentDto putBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto newBaseMajor) throws NotFoundException, ConflictException {
        DepartmentEntity targetEntity = getDepartmentEntity(universityCode, departmentCode);
        MajorEntity newMajorEntity = getMajorEntityFromDto(newBaseMajor);

        for(MajorDepartmentJoinEntity majorDepartmentJoin : targetEntity.getMajorDepartmentJoinEntities()) {
            if(majorDepartmentJoin.getMajorId() == newMajorEntity.getId()) {
                throw new ConflictException();
            }
        }

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
    public DepartmentDto removeBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto targetBaseMajor) throws NotFoundException {
        DepartmentEntity targetEntity = getDepartmentEntity(universityCode, departmentCode);
        MajorEntity targetBaseMajorEntity = getMajorEntityFromDto(targetBaseMajor);

        for(MajorDepartmentJoinEntity majorDepartmentJoin : targetEntity.getMajorDepartmentJoinEntities()) {
            if(majorDepartmentJoin.getMajorId() == targetBaseMajorEntity.getId()) {
                majorDepartmentJoinRepository.delete(majorDepartmentJoin);
                break;
            }
        }

        return targetEntity.toDepartmentDto();
    }

    private DepartmentEntity getDepartmentEntity(String universityCode, String departmentCode) throws NotFoundException {
        int universityIdForTarget = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        return IndexAdapter.getDepartmentEntity(departmentRepository, universityIdForTarget, departmentCode);
    }

    private MajorEntity getMajorEntityFromDto(MajorDto majorDto) throws NotFoundException {
        String universityCode = majorDto.getUniversity().getCode();
        String majorCode = majorDto.getCode();

        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        return IndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
    }
}
