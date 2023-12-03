package com.yamankwefati.webshopapi.seeder;

import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.model.Role;
import com.yamankwefati.webshopapi.model.User;
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
                    .userRol(Role.CUSTOMER)
                    .build();


            this.userDAO.saveNewUser(newUser);
        }
        User newUser = User.builder()
                .firstname("YamanAdmin")
                .lastname("KwefatiAdmin")
                .email("Admin@gmail.com")
                .password(passwordEncoder.encode("testPassword"))
                .phoneNumber("0625112776")
                .city("Bodegraven")
                .street("Koninginneweg 124")
                .postalCode("2411XV")
                .userRol(Role.ADMIN)
                .build();
        this.userDAO.saveNewUser(newUser);
    }
}
