package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.auth.AuthenticationRequest;
import com.yamankwefati.webshopapi.model.auth.AuthenticationResponse;
import com.yamankwefati.webshopapi.model.auth.RegisterRequest;
import com.yamankwefati.webshopapi.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return new ApiResponse<>(HttpStatus.ACCEPTED, service.register(request));
    }

    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return new ApiResponse<>(HttpStatus.ACCEPTED, service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
