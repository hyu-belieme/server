package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.HistoryEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HistoryRepository extends
    RefreshRepository<HistoryEntity, UUID> {

  @Query("select h from HistoryEntity h where h.item.stuff.departmentId = :deptId order by h.requestedAt desc, h.lostAt desc, h.id asc")
  List<HistoryEntity> findByDepartmentId(@Param("deptId") UUID deptId);

  @Query("select h from HistoryEntity h where h.item.stuff.departmentId = :deptId and h.requester.id = :requesterId order by h.requestedAt desc, h.lostAt desc, h.id asc")
  List<HistoryEntity> findByDepartmentIdAndRequesterId(
      @Param("deptId") UUID deptId, @Param("requesterId") UUID requesterId);

  Optional<HistoryEntity> findByItemIdAndNum(UUID itemId, int num);

  boolean existsByItemIdAndNum(UUID itemId, int num);
}
