package com.example.beliemeserver.config.cors;

import com.example.beliemeserver.config.cors.container.CorsRegistrationInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
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
