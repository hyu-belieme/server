package com.example.beliemeserver.data.repository.old;

import com.example.beliemeserver.data.entity.old.OldUserEntity;
import com.example.beliemeserver.data.entity.id.OldUserId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OldUserRepository extends RefreshRepository<OldUserEntity, OldUserId> {
    @EntityGraph(attributePaths = "authorities")
    Optional<OldUserEntity> findWithAuthoritiesByToken(String token);

    @Query("select u from OldUserEntity u where u.studentId = ?1")
    @EntityGraph(attributePaths = "authorities")
    Optional<OldUserEntity> findWithAuthoritiesByStudentId(String studentId);
}
