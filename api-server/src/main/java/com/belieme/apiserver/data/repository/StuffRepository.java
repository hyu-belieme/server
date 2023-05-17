package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.StuffEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StuffRepository extends RefreshRepository<StuffEntity, UUID> {

  List<StuffEntity> findByDepartmentId(UUID departmentId);

  Optional<StuffEntity> findByDepartmentIdAndName(UUID departmentId, String name);

  boolean existsByDepartmentIdAndName(UUID departmentId, String name);
}
