package com.example.beliemeserver.config.initdata._new.container;

import java.util.List;
import java.util.UUID;

public record DepartmentInfo(
        UUID id,
        String universityName,
        String name,
        List<MajorInfo> baseMajors) {

}