package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.*;
import com.example.beliemeserver.model.dao.MajorDao;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MajorDaoImpl extends BaseDaoImpl implements MajorDao {

    public MajorDaoImpl(UniversityRepository universityRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MajorRepository majorRepository, MajorUserJoinRepository majorUserJoinRepository, MajorDepartmentJoinRepository majorDepartmentJoinRepository, AuthorityRepository authorityRepository, AuthorityUserJoinRepository authorityUserJoinRepository, StuffRepository stuffRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        super(universityRepository, departmentRepository, userRepository, majorRepository, majorUserJoinRepository, majorDepartmentJoinRepository, authorityRepository, authorityUserJoinRepository, stuffRepository, itemRepository, historyRepository);
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
        String universityCode = newMajor.university().code();
        UniversityEntity university = getUniversityEntity(universityCode);

        checkMajorConflict(university.getId(), newMajor.code());

        MajorEntity newMajorEntity = new MajorEntity(
                university,
                newMajor.code()
        );

        MajorEntity savedMajorEntity = majorRepository.save(newMajorEntity);
        return savedMajorEntity.toMajorDto();
    }

    @Override
    public MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws DataException, NotFoundException, ConflictException {
        MajorEntity target = getMajorEntity(universityCode, majorCode);

        UniversityEntity newUniversity = getUniversityEntity(newMajor.university().code());

        checkMajorConflict(newUniversity.getId(), newMajor.code());

        target.setUniversity(newUniversity)
                .setCode(newMajor.code());

        return target.toMajorDto();
    }

    private void checkMajorConflict(int universityId, String majorCode) throws ConflictException {
        if(majorRepository.existsByUniversityIdAndCode(universityId, majorCode)) {
            throw new ConflictException();
        }
    }
}
