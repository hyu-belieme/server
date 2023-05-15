package com.belieme.apiserver.domain.dao;

import com.belieme.apiserver.domain.dto.MajorDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface MajorDao {
    @Transactional
    List<MajorDto> getAllList();

    @Transactional
    MajorDto getById(@NonNull UUID majorId);

    @Transactional
    MajorDto getByIndex(@NonNull UUID universityId, @NonNull String majorCode);

    @Transactional
    MajorDto create(@NonNull UUID majorId, @NonNull UUID universityId, @NonNull String majorCode);

    @Transactional
    MajorDto update(@NonNull UUID majorId, @NonNull UUID universityId, @NonNull String majorCode);
}
