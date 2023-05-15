package com.belieme.apiserver.config.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
public record ApiConfig(
        Keyword keyword,
        Variable variable,
        Query query,
        Header header
) {
    public record Keyword(
            String university,
            String department,
            String user,
            String myInfo,
            String loginExternalApi,
            String stuff,
            String item,
            String history,
            String doReserve,
            String doApprove,
            String doReturn,
            String doLost,
            String doCancel,
            String universityIndex,
            String departmentIndex,
            String userIndex,
            String stuffIndex,
            String itemIndex,
            String historyIndex
    ) {

    }

    public record Variable(
            String universityIndex,
            String departmentIndex,
            String userIndex,
            String stuffIndex,
            String itemIndex,
            String historyIndex
    ) {

    }

    public record Query(
            String requesterId,
            String departmentId,
            String stuffId,
            String itemId
    ) {

    }

    public record Header(
            String userToken,
            String externalApiToken
    ) {
    }

}
