package com.belieme.apiserver.config;

import com.belieme.apiserver.config.cors.container.CorsRegistrationInfo;
import com.belieme.apiserver.config.cors.CorsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final CorsConfig corsConfig;

    public WebMvcConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for(CorsRegistrationInfo registrationInfo : corsConfig.getRegistrationInfos()) {
            CorsRegistration registration = registry.addMapping(registrationInfo.mapping());
            if(registrationInfo.allowedOrigins() != null) registration = registration.allowedOrigins(registrationInfo.allowedOrigins());
            if(registrationInfo.allowedMethods() != null) registration = registration.allowedMethods(registrationInfo.allowedMethods());
            if(registrationInfo.allowedHeaders() != null) registration = registration.allowedHeaders(registrationInfo.allowedHeaders());
            if(registrationInfo.exposedHeaders() != null) registration = registration.exposedHeaders(registrationInfo.exposedHeaders());
            if(registrationInfo.allowCredentials() != null) registration = registration.allowCredentials(registrationInfo.allowCredentials());
            if(registrationInfo.maxAge() != null) registration.maxAge(registrationInfo.maxAge());
        }
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver locale = new SessionLocaleResolver();
        locale.setDefaultLocale(Locale.KOREA);
        return locale;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor locale = new LocaleChangeInterceptor();
        locale.setParamName("lang");
        return locale;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
