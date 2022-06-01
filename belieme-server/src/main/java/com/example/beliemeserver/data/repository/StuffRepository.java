package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.entity.id.StuffId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StuffRepository extends CrudRepository<StuffEntity, StuffId> {
    boolean existsByName(String name);
    Optional<StuffEntity> findByName(String name);
}
