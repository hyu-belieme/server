package com.example.beliemeserver.config.initdata._new.container;

import java.util.List;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String apiToken,
        UUID universityId,
        String studentId,
        String name,
        int entranceYear,
        List<AuthorityInfo> authorities) {

}
