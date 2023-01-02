package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.UniversityRepository;
import com.example.beliemeserver.model.dao.UniversityDao;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniversityDaoImpl implements UniversityDao {
    private final UniversityRepository universityRepository;

    public UniversityDaoImpl(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @Override
    public List<UniversityDto> getAllUniversitiesData() throws DataException {
        List<UniversityDto> output = new ArrayList<>();

        for (UniversityEntity universityEntity : universityRepository.findAll()) {
            output.add(universityEntity.toUniversityDto());
        }
        return output;
    }

    @Override
    public UniversityDto getUniversityByCodeData(String universityCode) throws DataException, NotFoundException {
        UniversityEntity targetUniversity = getUniversityEntity(universityCode);
        return targetUniversity.toUniversityDto();
    }

    @Override
    public UniversityDto addUniversityData(UniversityDto newUniversity) throws DataException, ConflictException {
        checkUniversityConflict(newUniversity.getCode());

        UniversityEntity newUniversityEntity = new UniversityEntity(
                newUniversity.getCode(),
                newUniversity.getName(),
                newUniversity.getApiUrl()
        );

        UniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
        return savedUniversityEntity.toUniversityDto();
    }

    @Override
    public UniversityDto updateUniversityData(String universityCode, UniversityDto newUniversity) throws DataException, NotFoundException, ConflictException {
        UniversityEntity target = getUniversityEntity(universityCode);
        if (doesIndexOfUniversityChange(universityCode, newUniversity)) {
            checkUniversityConflict(newUniversity.getCode());
        }

        target.setCode(newUniversity.getCode())
                .setName(newUniversity.getName())
                .setApiUrl(newUniversity.getApiUrl());

        return target.toUniversityDto();
    }

    private boolean doesIndexOfUniversityChange(String oldUniversityCode, UniversityDto newUniversity) {
        return !oldUniversityCode.equals(newUniversity.getCode());
    }

    private void checkUniversityConflict(String universityCode) throws ConflictException {
        if (universityRepository.existsByCode(universityCode)) {
            throw new ConflictException();
        }
    }

    private UniversityEntity getUniversityEntity(String universityCode) throws NotFoundException {
        return IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode);
    }
}
