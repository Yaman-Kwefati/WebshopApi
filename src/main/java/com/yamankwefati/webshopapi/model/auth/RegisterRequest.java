package com.yamankwefati.webshopapi.model.auth;

import com.yamankwefati.webshopapi.model.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;
    private String city;
    private String street;
    private String postalCode;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role userRol = Role.CUSTOMER;
}
