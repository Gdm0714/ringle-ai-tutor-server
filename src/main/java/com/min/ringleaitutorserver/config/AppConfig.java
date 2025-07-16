package com.min.ringleaitutorserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    
    private Cors cors = new Cors();
    
    @Data
    public static class Cors {
        private String[] allowedOrigins = {"http://localhost:3000"};
    }
}