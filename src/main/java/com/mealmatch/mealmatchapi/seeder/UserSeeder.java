package com.mealmatch.mealmatchapi.seeder;

import com.mealmatch.mealmatchapi.dao.user.UserDAO;
import com.mealmatch.mealmatchapi.model.Role;
import com.mealmatch.mealmatchapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSeeder {
    @Autowired
    private UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    public void seed(){
        for (int i = 0; i < 10; i++) {
            User newUser = User.builder()
                    .firstname("User" + i + "firstname")
                    .lastname("User" + i + "lastname")
                    .email("test"+i+"@gmail.com")
                    .password(passwordEncoder.encode("testPassword"))
                    .phoneNumber("0625112776")
                    .city("Bodegraven")
                    .street("Koninginneweg 124")
                    .postalCode("2411XV")
                    .userRol(Role.RECIPIENT)
                    .build();


            this.userDAO.saveNewUser(newUser);
        }
    }
}
