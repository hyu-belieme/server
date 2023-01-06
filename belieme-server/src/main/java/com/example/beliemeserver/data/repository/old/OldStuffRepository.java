package com.example.beliemeserver.data.repository.old;

import com.example.beliemeserver.data.entity.old.OldStuffEntity;
import com.example.beliemeserver.data.entity.id.OldStuffId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface OldStuffRepository extends RefreshRepository<OldStuffEntity, OldStuffId> {
    boolean existsByName(String name);
    Optional<OldStuffEntity> findByName(String name);
}
