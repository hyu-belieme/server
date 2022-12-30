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
    public UniversityDto getUniversityByCodeData(String code) throws DataException, NotFoundException {
        UniversityEntity targetUniversity = IndexAdapter.getUniversityEntityByCode(universityRepository,code);
        return targetUniversity.toUniversityDto();
    }

    @Override
    public UniversityDto addUniversityData(UniversityDto newUniversity) throws DataException, ConflictException {
        if(universityRepository.existsByCode(newUniversity.getCode())) {
            throw new ConflictException();
        }

        UniversityEntity newUniversityEntity = new UniversityEntity(
                newUniversity.getCode(),
                newUniversity.getName(),
                newUniversity.getApiUrl()
        );

        UniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
        return savedUniversityEntity.toUniversityDto();
    }

    @Override
    public UniversityDto updateUniversityData(String code, UniversityDto newUniversity) throws DataException, NotFoundException {
        UniversityEntity targetUniversity = IndexAdapter.getUniversityEntityByCode(universityRepository,code);

        UniversityEntity newUniversityEntity = new UniversityEntity(
                targetUniversity.getId(),
                newUniversity.getCode(),
                newUniversity.getName(),
                newUniversity.getApiUrl()
        );

        UniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
        return savedUniversityEntity.toUniversityDto();
    }
}
