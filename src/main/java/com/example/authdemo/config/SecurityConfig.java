package com.example.authdemo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.authdemo.security.HttpCookieOAuth2AuthorizationRequestRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ✅ CSRF OFF (SPA + OAuth)
            .csrf(csrf -> csrf.disable())

            // ✅ CORS ENABLED HERE ONLY
            .cors(cors -> {})

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/login",
                    "/login/**",
                    "/oauth2/**"
                ).permitAll()
                .anyRequest().authenticated()
            )

            // ✅ OAUTH FIX (COOKIE BASED)
            .oauth2Login(oauth -> oauth
                .authorizationEndpoint(auth -> auth
                    .authorizationRequestRepository(
                        new HttpCookieOAuth2AuthorizationRequestRepository()
                    )
                )
                .defaultSuccessUrl(
                    "https://yoursecurenotevaultapp.netlify.app/dashboard",
                    true
                )
            )

            // ✅ LOGOUT FIX (NO REDIRECT, JUST 200 OK)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, res, auth) -> {
                    res.setStatus(200);
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    // 🔥 SINGLE SOURCE OF TRUTH FOR CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://yoursecurenotevaultapp.netlify.app"
        ));

        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
