package com.example.beliemeserver.data.daoimpl.util;

import com.example.beliemeserver.data.entity.DataEntity;
import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.MajorRepository;
import com.example.beliemeserver.data.repository.UniversityRepository;
import com.example.beliemeserver.model.exception.NotFoundException;

public class IndexAdapter {
    public static UniversityEntity getUniversityEntityByCode(UniversityRepository universityRepository, String code) throws NotFoundException {
        UniversityEntity target = universityRepository.findByCode(code).orElse(null);
        return (UniversityEntity) checkNullAndReturn(target);
    }

    public static MajorEntity getMajorEntity(MajorRepository majorRepository, int universityId, String majorCode) throws NotFoundException {
        MajorEntity target = majorRepository.findByUniversityIdAndCode(universityId, majorCode).orElse(null);
        return (MajorEntity) checkNullAndReturn(target);
    }

    private static DataEntity checkNullAndReturn(DataEntity target) throws NotFoundException {
        if (target == null) {
            throw new NotFoundException();
        }
        return target;
    }
}
