package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.UniversityEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends RefreshRepository<UniversityEntity, UUID> {
    boolean existsByName(String name);

    Optional<UniversityEntity> findByName(String name);
}
