package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.UniversityDao;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UniversityDaoImpl extends BaseDaoImpl implements UniversityDao {

    @Autowired
    public UniversityDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
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
        checkUniversityConflict(newUniversity.code());

        UniversityEntity newUniversityEntity = new UniversityEntity(
                newUniversity.code(),
                newUniversity.name(),
                newUniversity.apiUrl()
        );

        UniversityEntity savedUniversityEntity = universityRepository.save(newUniversityEntity);
        return savedUniversityEntity.toUniversityDto();
    }

    @Override
    public UniversityDto updateUniversityData(String universityCode, UniversityDto newUniversity) throws DataException, NotFoundException, ConflictException {
        UniversityEntity target = getUniversityEntity(universityCode);
        if (doesIndexOfUniversityChange(universityCode, newUniversity)) {
            checkUniversityConflict(newUniversity.code());
        }

        target.setCode(newUniversity.code())
                .setName(newUniversity.name())
                .setApiUrl(newUniversity.apiUrl());

        return target.toUniversityDto();
    }

    private boolean doesIndexOfUniversityChange(String oldUniversityCode, UniversityDto newUniversity) {
        return !oldUniversityCode.equals(newUniversity.code());
    }

    private void checkUniversityConflict(String universityCode) throws ConflictException {
        if (universityRepository.existsByCode(universityCode)) {
            throw new ConflictException();
        }
    }
}
