package com.example.beliemeserver.config.initdata.container;

import java.util.List;

public record UserInfo(
        String apiToken,
        String universityCode,
        String studentId,
        String name,
        int entranceYear,
        List<AuthorityInfo> authorities) {

}
