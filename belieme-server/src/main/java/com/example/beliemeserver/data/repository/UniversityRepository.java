package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface UniversityRepository extends RefreshRepository<UniversityEntity, UUID> {
    boolean existsByName(String name);

    Optional<UniversityEntity> findByName(String name);
}
