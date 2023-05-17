package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.UserEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends RefreshRepository<UserEntity, UUID> {
    List<UserEntity> findByUniversityId(UUID universityId);

    Optional<UserEntity> findByUniversityIdAndStudentId(UUID universityId, String studentId);

    Optional<UserEntity> findByToken(String token);

    boolean existsByUniversityIdAndStudentId(UUID universityId, String studentId);
}
