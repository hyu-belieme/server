package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

public interface UserRepository extends RefreshRepository<UserEntity, Integer> {
}
