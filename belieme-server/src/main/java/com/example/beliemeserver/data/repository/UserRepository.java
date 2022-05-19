package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findWithAuthoritiesByToken(String token);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findWithAuthoritiesByStudentId(String studentId);
}
