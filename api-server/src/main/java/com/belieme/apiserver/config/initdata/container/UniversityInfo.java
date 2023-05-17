package com.belieme.apiserver.config.initdata.container;

import java.util.Map;
import java.util.UUID;

public record UniversityInfo(
        UUID id,
        String name,
        Map<String, String> externalApiInfo) {

}
