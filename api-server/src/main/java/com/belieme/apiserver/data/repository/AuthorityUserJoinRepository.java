package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.AuthorityUserJoinEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

public interface AuthorityUserJoinRepository extends
    RefreshRepository<AuthorityUserJoinEntity, Integer> {
}
