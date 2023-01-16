package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends RefreshRepository<UserEntity, Integer> {
    List<UserEntity> findByUniversityId(int universityId);
    Optional<UserEntity> findByUniversityIdAndStudentId(int universityId, String studentId);
    Optional<UserEntity> findByToken(String token);
}
