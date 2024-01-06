package com.yamankwefati.webshopapi.controller;

import ch.qos.logback.core.model.Model;
import com.yamankwefati.webshopapi.dao.confirmationToken.ConfirmationTokenDAO;
import com.yamankwefati.webshopapi.dao.email.EmailDAO;
import com.yamankwefati.webshopapi.dao.resetToken.PasswordResetTokenDAO;
import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.dao.user.UserRepository;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.User;
import com.yamankwefati.webshopapi.model.auth.*;
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
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;
    @Autowired
    private final ConfirmationTokenDAO confirmationTokenDAO;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;
    @Autowired
    private EmailDAO emailDAO;
    @Autowired
    private UserDAO userDAO;

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

    @PostMapping("/resetPassword")
    public ApiResponse<?> resetPassword(HttpServletRequest request,
                                         @RequestParam("email") String userEmail) {
        Optional<User> user = this.userRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "User not found");
        }
        String token = UUID.randomUUID().toString();
        this.passwordResetTokenDAO.createPasswordResetTokenForUser(user.get(), token);

        String link = "http://159.223.236.93/reset-password?token="+token;
        emailDAO.send(userEmail, passwordResetTokenDAO.buildEmail(user.get().getLastname(), link), "Password Change Request");
        return new ApiResponse<>(HttpStatus.OK, "Email sent!");
    }

    @GetMapping("/changePassword")
    public ApiResponse<String> showChangePasswordPage(@RequestParam("token") String token) {
        String result = passwordResetTokenDAO.validatePasswordResetToken(token);
        System.out.println(" result"+ result);
        System.out.println(" token"+ token);
        if(result != null) {
            return new ApiResponse<>(HttpStatus.OK, result);
        } else {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "token not found!");
        }
    }

    @PostMapping("/savePassword")
    public ApiResponse<?> savePassword(@RequestBody PasswordDto passwordDto) {

        System.out.println(passwordDto);
        String result = passwordResetTokenDAO.validatePasswordResetToken(passwordDto.getToken());
        System.out.println(result);
        if(!result.equals(passwordDto.getToken())) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Not found");
        }

        Optional<PasswordResetToken> token = passwordResetTokenDAO.getToken(passwordDto.getToken());
        if(token.isPresent()) {
            userDAO.changeUserPassword(token.get().getAppUser(), passwordDto.getNewPassword());
            return new ApiResponse<>(HttpStatus.OK, "Password Changed");
        } else {
            return new ApiResponse<>(HttpStatus.NOT_ACCEPTABLE, "Invalid");
        }
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
