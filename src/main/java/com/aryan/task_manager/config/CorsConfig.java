package com.aryan.task_manager.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${APP_CORS_ALLOWED_ORIGINS:http://localhost:5173}")
    private String allowedOrigins;
    

    @Override
    public void addCorsMappings(
            CorsRegistry registry) {

        String[] origins = Arrays.stream(
                        allowedOrigins.split(",")
                )
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toArray(String[]::new);

        registry
                .addMapping("/**")
                .allowedOrigins(
                        origins
                )
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
                .allowedHeaders("*");
    }
}