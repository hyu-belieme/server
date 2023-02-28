package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface MajorRepository extends RefreshRepository<MajorEntity, Integer> {
    boolean existsByUniversityIdAndCode(int universityId, String code);

    Optional<MajorEntity> findByUniversityIdAndCode(int universityId, String code);
}
