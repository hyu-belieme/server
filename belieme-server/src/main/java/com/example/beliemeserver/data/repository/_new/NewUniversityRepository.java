package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewUniversityEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewUniversityRepository extends RefreshRepository<NewUniversityEntity, UUID> {
    boolean existsByName(String name);

    Optional<NewUniversityEntity> findByName(String name);
}
