package com.themart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// src/main/java/com/themart/config/WebConfig.java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}