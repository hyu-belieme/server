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
        UniversityEntity university = getUniversityEntity(universityCode);

        checkMajorConflict(university.getId(), newMajor.getCode());

        MajorEntity newMajorEntity = new MajorEntity(
                university,
                newMajor.getCode()
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws DataException, NotFoundException, ConflictException {
        MajorEntity target = getMajorEntity(universityCode, majorCode);

        UniversityEntity newUniversity = getUniversityEntity(newMajor.getUniversity().getCode());

        checkMajorConflict(newUniversity.getId(), newMajor.getCode());

        target.setUniversity(newUniversity)
                .setCode(newMajor.getCode());

        return target.toMajorDto();
    }

    private void checkMajorConflict(int universityId, String majorCode) throws ConflictException {
        if(majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }
    }

    private UniversityEntity getUniversityEntity(String universityCode) throws NotFoundException {
        return IndexAdapter.getUniversityEntityByCode(universityRepository, universityCode);
    }

    private MajorEntity getMajorEntity(String universityCode, String majorCode) throws NotFoundException {
        int universityId = getUniversityEntity(universityCode).getId();
        return IndexAdapter.getMajorEntity(majorRepository, universityId, majorCode);
    }
}
