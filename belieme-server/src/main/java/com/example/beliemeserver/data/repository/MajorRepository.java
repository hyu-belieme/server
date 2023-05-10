package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.MajorEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface MajorRepository extends RefreshRepository<MajorEntity, UUID> {
    boolean existsByUniversityIdAndCode(UUID universityId, String code);

    Optional<MajorEntity> findByUniversityIdAndCode(UUID universityId, String code);
}
