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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                                .requestMatchers("/api/v1/payment/**").permitAll()
                                .requestMatchers("/api/v1/auth/refresh-token").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/users/all-users").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/v1/users/{userId}/**").access(userSecurity)
                                //Orders
                                .requestMatchers(HttpMethod.GET,"/api/v1/orders/all-orders").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/{orderId}").access(orderSecurity)
                                .requestMatchers(HttpMethod.PUT, "/api/v1/orders/{orderId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/user/{email}").access(orderUserSecurity)
                                .requestMatchers(HttpMethod.POST,"/api/v1/orders/new-order").hasAnyAuthority("ADMIN", "CUSTOMER")
                                //Categories
                                .requestMatchers(HttpMethod.GET,"/api/v1/categories/all-categories").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/categories/{categoryName}").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/categories/new-category").hasAuthority("ADMIN")
                                .requestMatchers( HttpMethod.DELETE,"/api/v1/categories/{categoryName}").hasAuthority("ADMIN")
                                //Products
                                .requestMatchers(HttpMethod.GET,"/api/v1/products/all-products").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/products/{productName}").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/products/new-product").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/products/{productId}").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/products/{productId}").hasAuthority("ADMIN")
                                //OrderItems
                                .requestMatchers(HttpMethod.GET,"/api/v1/order-items/all").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/v1/order-items/{orderItemId}").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.GET,"/api/v1/order-items/by-order/{orderId}").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.POST,"/api/v1/order-items/new-order-item").hasAnyAuthority("ADMIN", "CUSTOMER")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/order-items/{orderItemId}").hasAnyAuthority("ADMIN", "CUSTOMER")
                                //pictures
                                .requestMatchers(HttpMethod.GET,"/api/v1/files/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/files/**").hasAuthority("ADMIN")
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
