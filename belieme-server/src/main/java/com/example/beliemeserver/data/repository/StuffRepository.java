package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;

public interface StuffRepository extends RefreshRepository<StuffEntity, Integer> {
    List<StuffEntity> findByDepartmentId(int departmentId);

    Optional<StuffEntity> findByDepartmentIdAndName(int departmentId, String name);

    boolean existsByDepartmentIdAndName(int departmentId, String name);
}
