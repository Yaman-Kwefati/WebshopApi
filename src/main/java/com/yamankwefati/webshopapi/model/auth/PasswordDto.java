package com.yamankwefati.webshopapi.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {
//    private String oldPassword;
    private  String token;
    private String newPassword;
}
