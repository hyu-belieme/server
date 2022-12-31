package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.daoimpl.util.IndexAdapter;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.MajorRepository;
import com.example.beliemeserver.data.repository.UniversityRepository;
import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MajorDaoImpl implements MajorDao {
    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;

    public MajorDaoImpl(UniversityRepository universityRepository, MajorRepository majorRepository) {
        this.universityRepository = universityRepository;
        this.majorRepository = majorRepository;
    }

    @Override
    public List<MajorDto> getAllMajorsData() throws DataException {
        List<MajorDto> output = new ArrayList<>();
        for(MajorEntity majorEntity : majorRepository.findAll()) {
            output.add(majorEntity.toMajorDto());
        }
        return output;
    }

    @Override
    public MajorDto addMajorData(MajorDto newMajor) throws DataException, NotFoundException, ConflictException {
        String universityCode = newMajor.getUniversity().getCode();

        UniversityEntity university = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode);
        String majorCode = newMajor.getCode();

        if(majorRepository.existsByUniversityIdAndCode(university.getId(), majorCode)) {
            throw new ConflictException();
        }

        MajorEntity newMajorEntity = new MajorEntity(
                university.getId(),
                majorCode,
                university
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws DataException, NotFoundException, ConflictException {
        UniversityEntity oldUniversity = IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode);
        MajorEntity target = IndexAdapter.getMajorEntity(majorRepository, oldUniversity.getId(), majorCode);

        String newUniversityCode = newMajor.getUniversity().getCode();
        UniversityEntity newUniversity = IndexAdapter.getUniversityEntityByCode(universityRepository, newUniversityCode);

        MajorEntity newMajorEntity = new MajorEntity(
                target.getId(),
                newUniversity.getId(),
                newMajor.getCode(),
                newUniversity
        );

        if(majorRepository.existsByUniversityIdAndCode(newUniversity.getId(), newMajor.getCode())) {
            throw new ConflictException();
        }

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }
}
