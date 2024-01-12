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
//        for (int i = 0; i < 20; i++) {
//            User newUser = User.builder()
//                    .firstname("User" + i + "firstname")
//                    .lastname("User" + i + "lastname")
//                    .email("test"+i+"@gmail.com")
//                    .password(passwordEncoder.encode("testPassword"))
//                    .phoneNumber("0625112776")
//                    .city("Bodegraven")
//                    .street("Koninginneweg 124")
//                    .postalCode("2411XV")
//                    .build();
//
//
//            this.userDAO.saveNewUser(newUser);
//        }
        User newUser = User.builder()
                .firstname("Yaman")
                .lastname("Kwefati")
                .email("yamankwefati@icloud.com")
                .password(passwordEncoder.encode("yamank77"))
                .phoneNumber("0625112776")
                .city("Bodegraven")
                .street("Koninginneweg 124")
                .postalCode("2411XV")
                .userRole(Role.ADMIN)
                .enabled(true)
                .build();
        newUser.isEnabled();
        this.userDAO.saveNewUser(newUser);
    }
}
