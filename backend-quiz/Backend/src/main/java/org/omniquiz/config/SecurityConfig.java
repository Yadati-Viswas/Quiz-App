package org.omniquiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000"));
                    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS")); // Include OPTIONS for preflight
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true); // Allow cookies if needed
                    return config;
                }))
                .csrf().disable() // Disable CSRF for simplicity (enable in production)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v1-api/users/**", "/v1-api/quiz/**").permitAll() // Cover signup and quiz endpoints
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}