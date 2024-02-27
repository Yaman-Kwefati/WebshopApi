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
        User newUser = User.builder()
                .firstname("Yaman")
                .lastname("Kwefati")
                .email("yamankwefati@icloud.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("0625112776")
                .city("Bodegraven")
                .street("Koninginneweg 124")
                .postalCode("2411XV")
                .userRole(Role.ADMIN)
                .enabled(true)
                .build();
        newUser.isEnabled();
        this.userDAO.saveNewUser(newUser);
        User admin = User.builder()
                .firstname("Osama")
                .lastname("Mahzea")
                .email("omahzea@icloud.com")
                .password(passwordEncoder.encode("osama1"))
                .phoneNumber("0629012024")
                .city("Bodegraven")
                .street("1672 Laan")
                .postalCode("2411XV")
                .userRole(Role.ADMIN)
                .enabled(true)
                .build();
        admin.isEnabled();
        this.userDAO.saveNewUser(admin);
    }
}
