package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.StuffDto;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface StuffDao {

  @Transactional
  List<StuffDto> getAllList();

  @Transactional
  List<StuffDto> getListByDepartment(@NonNull UUID departmentId);

  @Transactional
  StuffDto getById(@NonNull UUID stuffId);

  @Transactional
  StuffDto create(@NonNull UUID stuffId, @NonNull UUID departmentId, @NonNull String name,
      String thumbnail, @NonNull String desc);

  @Transactional
  StuffDto update(@NonNull UUID stuffId, @NonNull UUID departmentId, @NonNull String name,
      String thumbnail, @NonNull String desc);
}
