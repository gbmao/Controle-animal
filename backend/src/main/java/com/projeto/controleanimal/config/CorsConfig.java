//package com.projeto.controleanimal.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Aplica a todos os endpoints
//                .allowedOrigins("http://localhost:5173", "https://controle-animal-production.up.railway.app",
//                        "https://patrulha-felina.netlify.app","http://localhost:8080","http://localhost:8888" ) // Liste todas as origens permitidas
//                .allowedMethods("*") // Permite todos os métodos (GET, POST, OPTIONS, etc.)
//                .allowedHeaders("*")
//                .allowCredentials(true); // ESSENCIAL: Permite cabeçalhos customizados como 'x-api-key'
//    }
//}
package com.projeto.controleanimal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig { // Não precisa mais de WebMvcConfigurer

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Colocando suas origens em uma lista
        List<String> allowedOrigins = Arrays.asList(
                "http://localhost:5173",
                "https://controle-animal-production.up.railway.app",
                "https://patrulha-felina.netlify.app",
                "http://localhost:8080",
                "http://localhost:8888",
                "https://controle-animal.onrender.com"
        );

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // ESSENCIAL para enviar o cookie JSESSIONID
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}