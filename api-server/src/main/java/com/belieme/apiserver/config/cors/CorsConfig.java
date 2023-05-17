package com.belieme.apiserver.config.cors;

import com.belieme.apiserver.config.cors.container.CorsRegistrationInfo;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import org.springframework.stereotype.Component;

@Setter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsConfig {
    private List<CorsRegistrationInfo> registrationInfos;

    public List<CorsRegistrationInfo> getRegistrationInfos() {
        return new ArrayList<>(registrationInfos);
    }
}
