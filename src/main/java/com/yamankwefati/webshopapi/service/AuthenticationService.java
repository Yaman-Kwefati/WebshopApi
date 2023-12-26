package com.yamankwefati.webshopapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamankwefati.webshopapi.dao.user.UserRepository;
import com.yamankwefati.webshopapi.model.auth.AuthenticationRequest;
import com.yamankwefati.webshopapi.model.auth.AuthenticationResponse;
import com.yamankwefati.webshopapi.model.auth.RegisterRequest;
import com.yamankwefati.webshopapi.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
        // Check if the user already exists
        Optional<User> existingUser = this.userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()){
            throw new RuntimeException("User Already exist");
        }

        // Create a new user
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .city(request.getCity())
                .street(request.getStreet())
                .postalCode(request.getPostalCode())
                .userRole(request.getUserRol())
                .build();
        userRepository.save(user);

        // Generate JWT tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Set tokens as HTTP-only cookies in the response
        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", jwtToken)
                .httpOnly(true)
                .secure(false) // set to true in production
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // same as above
                .path("/")
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        // Return the response without the tokens in the body
        return AuthenticationResponse.builder()
                .user(user)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", jwtToken)
                .httpOnly(true)
                .secure(false) // should be true in production
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken",  refreshToken)
                .httpOnly(true)
                .secure(false) // same as above
                .path("/")
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return AuthenticationResponse.builder()
                .user(user)
                // remove accessToken and refreshToken from the response
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String refreshToken = getRefreshTokenFromCookies(request);

        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var userDetails = this.userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isRefreshTokenValid(refreshToken)) {
                var newAccessToken = jwtService.generateToken(userDetails);
                var newRefreshToken = jwtService.generateRefreshToken(userDetails);

                setTokenCookie(response, "accessToken", newAccessToken);
                setTokenCookie(response, "refreshToken", newRefreshToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void setTokenCookie(HttpServletResponse response, String name, String token) {
        ResponseCookie cookie = ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(false) // set to true in production
                .path("/")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }


}
