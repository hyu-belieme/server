package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.data.entity.id.UserId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends RefreshRepository<UserEntity, UserId> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findWithAuthoritiesByToken(String token);

    @Query("select u from UserEntity u where u.studentId = ?1")
    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findWithAuthoritiesByStudentId(String studentId);
}
