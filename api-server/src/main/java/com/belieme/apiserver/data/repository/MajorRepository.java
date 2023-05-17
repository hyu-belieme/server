package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.MajorEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;
import java.util.Optional;
import java.util.UUID;

public interface MajorRepository extends RefreshRepository<MajorEntity, UUID> {

  boolean existsByUniversityIdAndCode(UUID universityId, String code);

  Optional<MajorEntity> findByUniversityIdAndCode(UUID universityId, String code);
}
