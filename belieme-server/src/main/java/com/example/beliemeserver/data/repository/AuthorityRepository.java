package com.example.beliemeserver.data.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.entity.id.AuthorityId;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, AuthorityId> {
}
