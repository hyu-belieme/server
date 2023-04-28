package com.example.beliemeserver.config.initdata._new.container;

import java.util.List;

public record UserInfo(
        String apiToken,
        String universityName,
        String studentId,
        String name,
        int entranceYear,
        List<AuthorityInfo> authorities) {

}
