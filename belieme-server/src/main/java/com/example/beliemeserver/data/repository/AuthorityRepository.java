package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface AuthorityRepository extends RefreshRepository<AuthorityEntity, Integer> {
    Optional<AuthorityEntity> findByDepartmentIdAndPermission(int departmentId, String permission);

    boolean existsByDepartmentIdAndPermission(int departmentId, String permission);
}
