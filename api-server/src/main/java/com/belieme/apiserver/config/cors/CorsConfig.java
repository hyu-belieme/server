package com.belieme.apiserver.config.cors;

import com.belieme.apiserver.config.cors.container.CorsRegistrationInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
public class CorsConfig {
    private final List<CorsRegistrationInfo> registrationInfos;

    public CorsConfig(List<CorsRegistrationInfo> registrationInfos) {
        this.registrationInfos = registrationInfos;
    }

    public List<CorsRegistrationInfo> registrationInfos() {
        return registrationInfos;
    }
}
