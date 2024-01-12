package com.yamankwefati.webshopapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamankwefati.webshopapi.dao.confirmationToken.ConfirmationTokenDAO;
import com.yamankwefati.webshopapi.dao.email.EmailDAO;
import com.yamankwefati.webshopapi.dao.email.EmailSender;
import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.dao.user.UserRepository;
import com.yamankwefati.webshopapi.model.ConfirmationToken;
import com.yamankwefati.webshopapi.model.auth.AuthenticationRequest;
import com.yamankwefati.webshopapi.model.auth.AuthenticationResponse;
import com.yamankwefati.webshopapi.model.auth.RegisterRequest;
import com.yamankwefati.webshopapi.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenDAO confirmationTokenDAO;
    private final EmailDAO emailDAO;

    public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
        Optional<User> existingUser = this.userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()){
            throw new IllegalStateException("User Already exist");
        }

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

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .appUser(user)
                .build();

        this.confirmationTokenDAO.saveConfirmationToken(confirmationToken);
        String link = "https://yaman-g.nl/api/v1/auth/register/confirm?token="+token+"&userId="+user.getId();
        this.emailDAO.send(request.getEmail(), buildEmail(request.getLastname(), link), "Confirm Your Email");

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);



        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", jwtToken)
                .httpOnly(true)
                .secure(true) // set to true in production
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
//                .sameSite("None")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // same as above
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
//                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

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
                .secure(true) // should be true in production
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
//                .sameSite("None")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken",  refreshToken)
                .httpOnly(true)
                .secure(true) // same as above
                .path("/")
                .maxAge(60 * 60 * 24 * 7)
//                .sameSite("None")
                .build();
        System.out.println(jwtCookie);
        System.out.println(refreshCookie);

        response.addHeader("Set-Cookie", jwtCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return AuthenticationResponse.builder()
                .user(user)
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

                setTokenCookie(response, "accessToken", newAccessToken, 60 * 60 * 24);
                setTokenCookie(response, "refreshToken", newRefreshToken, 60 * 60 * 24 * 7);
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

    private void setTokenCookie(HttpServletResponse response, String name, String token, long maxAgeSecons) {
        ResponseCookie cookie = ResponseCookie.from(name, token)
                .httpOnly(true)
                .secure(true) // set to true in production
                .path("/")
//                .sameSite("None")
                .maxAge(maxAgeSecons)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void logoutUser(HttpServletResponse response){
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private String buildEmail(String userName, String link){
        return "\n" +
                "Hello "+userName+",\n" +
                "\n" +
                "Please click on the following link to confirm you registration for Our Company\n" +
                ""+link+"\n" +
                "\n" +
                "See you there!\n" +
                "\n" +
                "Best regards, Our Company team";
    }
}
