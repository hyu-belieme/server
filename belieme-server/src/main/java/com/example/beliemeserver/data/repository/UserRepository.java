package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.data.entity.id.UserId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, UserId> {
    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findWithAuthoritiesByToken(String token);

    @EntityGraph(attributePaths = "authorities")
    Optional<UserEntity> findWithAuthoritiesByStudentId(String studentId);
}
