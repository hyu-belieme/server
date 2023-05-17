package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface UserDao {

  @Transactional
  List<UserDto> getAllList();

  @Transactional
  List<UserDto> getListByUniversity(@NonNull UUID universityId);

  @Transactional
  UserDto getByToken(@NonNull String token);

  @Transactional
  UserDto getById(@NonNull UUID userId);

  @Transactional
  UserDto getByIndex(@NonNull UUID universityId, @NonNull String studentId);

  @Transactional
  UserDto create(@NonNull UUID userId, @NonNull UUID universityId, @NonNull String studentId,
      @NonNull String name, int entranceYear, @NonNull String token, long createdAt,
      long approvedAt, @NonNull List<AuthorityDto> authorities);

  @Transactional
  UserDto update(@NonNull UUID userId, @NonNull UUID universityId, @NonNull String studentId,
      @NonNull String name, int entranceYear, @NonNull String token, long createdAt,
      long approvedAt, @NonNull List<AuthorityDto> authorities);
}
