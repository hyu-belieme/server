package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.UniversityDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
