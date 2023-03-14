package com.example.beliemeserver.config.initdata.container;

import java.util.List;

public record DepartmentInfo(
        String universityCode,
        String code,
        String name,
        List<String> baseMajors) {

}