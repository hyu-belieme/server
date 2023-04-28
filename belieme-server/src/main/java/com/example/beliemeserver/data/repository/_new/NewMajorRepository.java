package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewMajorEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewMajorRepository extends RefreshRepository<NewMajorEntity, UUID> {
    boolean existsByUniversityIdAndCode(UUID universityId, String code);

    Optional<NewMajorEntity> findByUniversityIdAndCode(UUID universityId, String code);
}
