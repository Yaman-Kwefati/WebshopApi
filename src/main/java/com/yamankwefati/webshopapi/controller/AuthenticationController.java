package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.confirmationToken.ConfirmationTokenDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.auth.AuthenticationRequest;
import com.yamankwefati.webshopapi.model.auth.AuthenticationResponse;
import com.yamankwefati.webshopapi.model.auth.RegisterRequest;
import com.yamankwefati.webshopapi.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;
    @Autowired
    private final ConfirmationTokenDAO confirmationTokenDAO;

    //Register a new user
    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response
    ){
        return new ApiResponse<>(HttpStatus.ACCEPTED, service.register(request, response));
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<?> confirmRegistration(
            @RequestParam("token") String token,
            @RequestParam("userId") Long userId
    ){
        try {
            this.confirmationTokenDAO.confirmToken(token, userId);
            return ResponseEntity.ok("Email successfully confirmed.");
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error confirming email: " + e.getMessage());
        }
    }


    //login a user
    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ){
        return new ApiResponse<>(HttpStatus.ACCEPTED, service.authenticate(request, response));
    }

    //refresh jwt token
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {
        service.logoutUser(response);
        return new ApiResponse<>(HttpStatus.OK, "Done");
    }

}
