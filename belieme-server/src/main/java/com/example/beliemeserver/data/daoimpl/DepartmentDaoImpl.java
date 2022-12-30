package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.entity.MajorDepartmentJoinEntity;
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
    public DepartmentDto putBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto newBaseMajor) throws NotFoundException {
        int universityIdForTarget = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        DepartmentEntity target = IndexAdapter.getDepartmentEntity(departmentRepository, universityIdForTarget, departmentCode);

        int newBaseMajorId = getMajorId(newBaseMajor);
        for(MajorDepartmentJoinEntity majorDepartmentJoin : target.getMajorDepartmentJoinEntities()) {
            if(majorDepartmentJoin.getMajorId() == newBaseMajorId) {
                departmentRepository.refresh(target);
                return target.toDepartmentDto();
            }
        }

        MajorDepartmentJoinEntity newJoin = new MajorDepartmentJoinEntity(
                newBaseMajorId,
                target.getId()
        );
        majorDepartmentJoinRepository.save(newJoin);

        departmentRepository.refresh(target);
        return target.toDepartmentDto();
    }

    @Override
    public DepartmentDto removeBaseMajorOnDepartmentData(String universityCode, String departmentCode, MajorDto targetBaseMajor) throws NotFoundException {
        int universityIdForTarget = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        DepartmentEntity target = IndexAdapter.getDepartmentEntity(departmentRepository, universityIdForTarget, departmentCode);

        int targetBaseMajorId = getMajorId(targetBaseMajor);
        for(MajorDepartmentJoinEntity majorDepartmentJoin : target.getMajorDepartmentJoinEntities()) {
            if(majorDepartmentJoin.getMajorId() == targetBaseMajorId) {
                majorDepartmentJoinRepository.delete(new MajorDepartmentJoinEntity(
                        targetBaseMajorId,
                        target.getId()
                ));
            }
        }

        departmentRepository.refresh(target);
        return target.toDepartmentDto();
    }

    private int getMajorId(MajorDto major) throws NotFoundException {
        String newBaseMajorCode = major.getCode();
        String universityCodeForNewBaseMajor = major.getUniversity().getCode();
        int universityIdForNewBaseMajor = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCodeForNewBaseMajor).getId();
        int newBaseMajorId = IndexAdapter.getMajorEntity(majorRepository, universityIdForNewBaseMajor, newBaseMajorCode).getId();

        return newBaseMajorId;
    }
}
