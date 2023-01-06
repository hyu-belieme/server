package com.example.beliemeserver.data.repository.old;

import com.example.beliemeserver.data.entity.old.OldItemEntity;
import com.example.beliemeserver.data.entity.id.OldItemId;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;

public interface OldItemRepository extends RefreshRepository<OldItemEntity, OldItemId> {
    List<OldItemEntity> findByStuffId(int stuffId);
}
