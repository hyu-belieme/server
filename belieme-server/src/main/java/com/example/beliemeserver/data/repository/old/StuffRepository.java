package com.example.beliemeserver.data.repository.old;

import com.example.beliemeserver.data.entity.old.StuffEntity;
import com.example.beliemeserver.data.entity.id.StuffId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface StuffRepository extends RefreshRepository<StuffEntity, StuffId> {
    boolean existsByName(String name);
    Optional<StuffEntity> findByName(String name);
}
