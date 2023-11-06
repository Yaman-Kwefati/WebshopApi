package com.mealmatch.mealmatchapi.controller;

import com.mealmatch.mealmatchapi.model.ApiResponse;
import com.mealmatch.mealmatchapi.model.auth.AuthenticationRequest;
import com.mealmatch.mealmatchapi.model.auth.AuthenticationResponse;
import com.mealmatch.mealmatchapi.model.auth.RegisterRequest;
import com.mealmatch.mealmatchapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
