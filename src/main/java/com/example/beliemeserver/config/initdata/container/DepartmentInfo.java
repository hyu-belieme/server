package com.example.beliemeserver.config.initdata.container;

import java.util.List;
import java.util.UUID;

public record DepartmentInfo(
        UUID id,
        UUID universityId,
        String name,
        List<MajorInfo> baseMajors) {

}