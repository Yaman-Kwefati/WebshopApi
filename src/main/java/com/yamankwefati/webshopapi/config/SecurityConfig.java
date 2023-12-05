package com.yamankwefati.webshopapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();
        http
                .authorizeHttpRequests()
                //Users
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users/all-users").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/users/{userId}").permitAll()
                //Orders
                .requestMatchers("/api/v1/orders/all-orders").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/orders/{orderId}").hasAnyAuthority("ADMIN", "CUSTOMER")
                .requestMatchers("/api/v1/orders/new-order").hasAnyAuthority("ADMIN", "CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
