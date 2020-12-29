package com.fwtai.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(final CorsRegistry registry){
        registry.addMapping("/**")
            .allowedOrigins("http://console.humpsaas.com","http://192.168.3.108")
            .allowedMethods("GET","POST","OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(7200L);
    }
}