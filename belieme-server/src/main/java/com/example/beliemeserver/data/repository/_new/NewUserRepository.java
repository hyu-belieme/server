package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewUserEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewUserRepository extends RefreshRepository<NewUserEntity, UUID> {
    List<NewUserEntity> findByUniversityId(UUID universityId);

    Optional<NewUserEntity> findByUniversityIdAndStudentId(UUID universityId, String studentId);

    Optional<NewUserEntity> findByToken(String token);

    boolean existsByUniversityIdAndStudentId(UUID universityId, String studentId);
}
