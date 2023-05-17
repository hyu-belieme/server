package com.belieme.apiserver.config.initdata.container;

import java.util.UUID;

public record AuthorityInfo(
        UUID departmentId,
        String permission) {

}
