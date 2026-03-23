package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backend.auth.JWTAuthorizationFilter;

@EnableWebSecurity()
@Configuration
public class WebSecurityConfig{

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;
    
        @Bean
        @Order(1)
        public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                        .securityMatcher("/api/**")
                        .csrf((csrf) -> csrf.disable())
                        .authorizeHttpRequests((requests) -> requests
                                .requestMatchers(HttpMethod.GET, "/api/patients").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/patients").hasAnyRole("USER", "VET", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/patients/delete/**").hasAnyRole("VET", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/appointments").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/appointments").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/appointments/delete/**").hasAnyRole("VET", "ADMIN")
                                .anyRequest().authenticated()
                        )
                        .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .csrf((csrf) -> csrf.disable())
                        .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/", "/home", "/login", "/error", "/favicon.ico").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/style.css", "/login.css", "/webjars/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/patients", "/appointments").permitAll()
                                .requestMatchers(HttpMethod.GET, "/patients/new", "/appointments/new").permitAll()
                                .requestMatchers(HttpMethod.POST, "/patients").hasAnyRole("USER", "VET", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/appointments").authenticated()
                                .anyRequest().authenticated()
                        );

                return http.build();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico");
        }
}
