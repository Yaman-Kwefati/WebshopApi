package com.yamankwefati.webshopapi.config;

import com.yamankwefati.webshopapi.service.OrderSecurity;
import com.yamankwefati.webshopapi.service.OrderUserSecurity;
import com.yamankwefati.webshopapi.service.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserSecurity userSecurity,
                                                   OrderSecurity orderSecurity, OrderUserSecurity orderUserSecurity) throws Exception {
        http = http.cors().and().csrf().disable();
        http.authorizeHttpRequests(
                auth -> {
                    try {
                        auth
                                //Users
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/api/v1/users/all-users").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/users/{userId}/**").access(userSecurity)
                                //Orders
                                .requestMatchers("/api/v1/orders/all-orders").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/{orderId}").access(orderSecurity)
                                .requestMatchers(HttpMethod.PUT, "/api/v1/orders/{orderId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/user/{email}").access(orderUserSecurity)
                                .requestMatchers("/api/v1/orders/new-order").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .anyRequest().authenticated()
                                .and()
                                .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return http.build();
    }
}
