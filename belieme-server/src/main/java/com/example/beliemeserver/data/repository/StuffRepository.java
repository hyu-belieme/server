package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface StuffRepository extends RefreshRepository<StuffEntity, Integer> {
    Optional<StuffEntity> findByDepartmentIdAndName(int departmentId, String name);
}
