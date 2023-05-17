package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.UniversityDto;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface UniversityDao {

  @Transactional
  List<UniversityDto> getAllList();

  @Transactional
  UniversityDto getById(@NonNull UUID id);

  @Transactional
  boolean checkExistById(@NonNull UUID id);

  @Transactional
  UniversityDto create(@NonNull UUID id, @NonNull String name, String apiUrl);

  @Transactional
  UniversityDto update(@NonNull UUID id, @NonNull String name, String apiUrl);
}
