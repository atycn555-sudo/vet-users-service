package com.example.vet.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    // Endpoints públicos (no requieren autenticación)
    private static final String[] WHITE_LIST_URL = {
        "/api/auth/register",
        "/api/auth/login",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authenticationProvider(authenticationProvider)
            .authorizeHttpRequests(req -> req
                // Permitir preflight CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Endpoints públicos
                .requestMatchers(WHITE_LIST_URL).permitAll()

                // USER + ADMIN pueden acceder a estos endpoints
                .requestMatchers(HttpMethod.GET,
                    "/api/v1/products/**",
                    "/api/v1/services/**",
                    "/api/v1/veterinarians/**",
                    "/api/v1/shifts/**"
                ).hasAnyRole("ADMIN", "USER")

                .requestMatchers("/api/v1/clients/**", "/api/v1/pets/**", "/api/v1/vaccines/**", "/api/v1/species/**")
                .hasAnyRole("ADMIN", "USER")

                .requestMatchers(HttpMethod.POST, "/api/v1/medical-history/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/medical-history/**").hasAnyRole("ADMIN", "USER")

                // SOLO ADMIN
                .requestMatchers(HttpMethod.PUT, "/api/v1/medical-history/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/products/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/services/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/veterinarians/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/shifts/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/suppliers/**").hasRole("ADMIN")

                // Endpoints de usuarios
                .requestMatchers("/api/users/all").hasRole("ADMIN")          // solo admin puede listar todos
                .requestMatchers("/api/users/by-email/**").hasRole("ADMIN")  // solo admin puede buscar por email
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "USER") // acceso general a otros endpoints

                // DEFAULT: cualquier otro requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()); // 👈 Basic Auth

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
