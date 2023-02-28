package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UniversityEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface UniversityRepository extends RefreshRepository<UniversityEntity, Integer> {
    boolean existsByCode(String code);

    Optional<UniversityEntity> findByCode(String code);
}
