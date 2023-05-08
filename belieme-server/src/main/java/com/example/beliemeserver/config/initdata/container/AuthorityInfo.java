package com.example.beliemeserver.config.initdata.container;

import java.util.UUID;

public record AuthorityInfo(
        UUID departmentId,
        String permission) {

}
