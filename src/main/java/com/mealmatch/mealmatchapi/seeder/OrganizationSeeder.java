package com.mealmatch.mealmatchapi.seeder;

import com.mealmatch.mealmatchapi.dao.organization.OrganizationDAO;
import com.mealmatch.mealmatchapi.dao.user.UserDAO;
import com.mealmatch.mealmatchapi.model.Organization;
import com.mealmatch.mealmatchapi.model.Role;
import com.mealmatch.mealmatchapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationSeeder {
    private final OrganizationDAO organizationDAO;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    public void seed(){
        User newUser = User.builder()
                .firstname("Yaman")
                .lastname("Kwefati")
                .email("yamankwefati20@gmail.com")
                .password(passwordEncoder.encode("testPassword"))
                .phoneNumber("0625112776")
                .city("Bodegraven")
                .street("Koninginneweg 124")
                .postalCode("2411XV")
                .userRol(Role.DONOR)
                .build();
        this.userDAO.saveNewUser(newUser);
        for (int i = 0; i < 10; i++) {
            Organization organization = Organization.builder()
                    .name("TestOrgan" + i)
                    .phone_number("0625112771")
                    .city("Bodegraven")
                    .street("Koninginneweg 124")
                    .postal_code("2411XV")
                    .type("Restaurant")
                    .description("nothing")
                    .contact_person(newUser)
                    .build();
            this.organizationDAO.saveNewOrganization(organization);
        }
    }
}
