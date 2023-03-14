package com.example.beliemeserver.config.initdata.container;

import java.util.Map;

public record UniversityInfo(
        String code,
        String name,
        Map<String, String> externalApiInfo) {

}
