package com.example.beliemeserver.config.initdata._new.container;

import java.util.Map;
import java.util.UUID;

public record UniversityInfo(
        UUID id,
        String name,
        Map<String, String> externalApiInfo) {

}
