package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.error.exception.ConflictException;
import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MajorDaoImpl extends BaseDaoImpl implements MajorDao {

    public MajorDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
    }

    @Override
    public List<MajorDto> getAllList() {
        List<MajorDto> output = new ArrayList<>();
        for (MajorEntity majorEntity : majorRepository.findAll()) {
            output.add(majorEntity.toMajorDto());
        }
        return output;
    }

    @Override
    public MajorDto getByIndex(String universityCode, String majorCode) {
        MajorEntity targetEntity = findMajorEntity(universityCode, majorCode);
        return targetEntity.toMajorDto();
    }

    @Override
    public MajorDto create(MajorDto newMajor) {
        UniversityEntity university = findUniversityEntity(newMajor.university());

        checkMajorConflict(university.getId(), newMajor.code());

        MajorEntity newMajorEntity = new MajorEntity(
                university,
                newMajor.code()
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto update(String universityCode, String majorCode, MajorDto newMajor) {
        MajorEntity target = findMajorEntity(universityCode, majorCode);

        UniversityEntity newUniversity = findUniversityEntity(newMajor.university());

        checkMajorConflict(newUniversity.getId(), newMajor.code());

        target.setUniversity(newUniversity)
                .setCode(newMajor.code());

        return target.toMajorDto();
    }

    private void checkMajorConflict(int universityId, String majorCode) {
        if (majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }
    }
}
