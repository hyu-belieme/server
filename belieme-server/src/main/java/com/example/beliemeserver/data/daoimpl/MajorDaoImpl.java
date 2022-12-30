package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.repository.MajorRepository;
import com.example.beliemeserver.data.repository.UniversityRepository;
import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

public class MajorDaoImpl implements MajorDao {
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    public MajorDaoImpl(UniversityRepository universityRepository, MajorRepository majorRepository) {
        this.universityRepository = universityRepository;
        this.majorRepository = majorRepository;
    }

    @Override
    public MajorDto addMajorData(MajorDto newMajor) throws DataException, NotFoundException, ConflictException {
        String universityCode = newMajor.getUniversity().getCode();

        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        String majorCode = newMajor.getCode();

        if(majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }

        MajorEntity newMajorEntity = new MajorEntity(
                universityId,
                majorCode
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws DataException, NotFoundException {
        int universityId = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode).getId();
        int targetId = IndexAdapter.getMajorEntity(majorRepository, universityId, majorCode).getId();

        String newUniversityCode = newMajor.getUniversity().getCode();
        int newUniversityId = IndexAdapter.getUniversityEntityByCode(universityRepository, newUniversityCode).getId();

        MajorEntity newMajorEntity = new MajorEntity(
                targetId,
                newUniversityId,
                newMajor.getCode()
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }
}
