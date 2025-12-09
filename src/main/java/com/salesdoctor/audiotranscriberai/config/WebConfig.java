package com.salesdoctor.audiotranscriberai.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**") // Barcha yo'llar uchun
                        .allowedOriginPatterns("*") // Barcha manbalar uchun
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Barcha metodlar
                        .allowedHeaders("*")
                        .allowCredentials(true); // Agar cookie/token yuborilsa kerak bo'ladi
            }
        };
    }
}