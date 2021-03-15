package com.fwtai.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Value("${app.origins}")
    private String origins;

    @Override
    public void addCorsMappings(final CorsRegistry registry){
        registry.addMapping("/**")
            .allowedOrigins(origins.split(","))
            .allowedMethods("GET","POST","OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(7200L);
    }
}