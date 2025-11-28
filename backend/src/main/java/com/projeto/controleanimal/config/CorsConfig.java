package com.projeto.controleanimal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos os endpoints
                .allowedOrigins("http://localhost:5173", "https://controle-animal-production.up.railway.app",
                        "https://patrulha-felina.netlify.app","http://localhost:8080","http://localhost:8888" ) // Liste todas as origens permitidas
                .allowedMethods("*") // Permite todos os métodos (GET, POST, OPTIONS, etc.)
                .allowedHeaders("*")
                .allowCredentials(true); // ESSENCIAL: Permite cabeçalhos customizados como 'x-api-key'
    }
}
