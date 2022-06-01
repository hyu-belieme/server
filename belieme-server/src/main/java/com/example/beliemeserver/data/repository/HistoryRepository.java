package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.HistoryEntity;
import com.example.beliemeserver.data.entity.id.HistoryId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<HistoryEntity, HistoryId> {
    List<HistoryEntity> findByRequesterId(String requesterId);
}
