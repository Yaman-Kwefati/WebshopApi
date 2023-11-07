package com.mealmatch.mealmatchapi.seeder;

import com.mealmatch.mealmatchapi.dao.organization.HoursOfOperationDAO;
import com.mealmatch.mealmatchapi.dao.organization.OrganizationDAO;
import com.mealmatch.mealmatchapi.dao.user.UserDAO;
import com.mealmatch.mealmatchapi.model.HoursOfOperation;
import com.mealmatch.mealmatchapi.model.Organization;
import com.mealmatch.mealmatchapi.model.Role;
import com.mealmatch.mealmatchapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class HoursOfOperationSeeder {
    @Autowired
    private HoursOfOperationDAO hoursOfOperationDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrganizationDAO organizationDAO;
    private final PasswordEncoder passwordEncoder;

    public void seed(){
        User newUser = User.builder()
                .firstname("YamanHours")
                .lastname("Kwefati")
                .email("yamankwefati21@gmail.com")
                .password(passwordEncoder.encode("testPassword"))
                .phoneNumber("0625112776")
                .city("Bodegraven")
                .street("Koninginneweg 124")
                .postalCode("2411XV")
                .userRol(Role.DONOR)
                .build();
        this.userDAO.saveNewUser(newUser);
        Organization organization = Organization.builder()
                .name("TestOrganforHours")
                .phone_number("0625112771")
                .city("Bodegraven")
                .street("Koninginneweg 125")
                .postal_code("2411XV")
                .type("Restaurant")
                .description("nothing")
                .contact_person(newUser)
                .build();
        this.organizationDAO.saveNewOrganization(organization);
        HoursOfOperation hoursOfOperation = HoursOfOperation.builder()
                .day_of_the_week("monday")
                .opening_time(Time.valueOf(LocalTime.of(8,0)))
                .closing_time(Time.valueOf(LocalTime.of(15,0)))
                .organizationId(organization)
                .build();
        HoursOfOperation hoursOfOperation1 = HoursOfOperation.builder()
                .day_of_the_week("tuesday")
                .opening_time(Time.valueOf(LocalTime.of(8,0)))
                .closing_time(Time.valueOf(LocalTime.of(15,0)))
                .organizationId(organization)
                .build();
        HoursOfOperation hoursOfOperation2 = HoursOfOperation.builder()
                .day_of_the_week("wednesday")
                .opening_time(Time.valueOf(LocalTime.of(8,0)))
                .closing_time(Time.valueOf(LocalTime.of(15,0)))
                .organizationId(organization)
                .build();
        HoursOfOperation hoursOfOperation3 = HoursOfOperation.builder()
                .day_of_the_week("thursday")
                .opening_time(Time.valueOf(LocalTime.of(8,0)))
                .closing_time(Time.valueOf(LocalTime.of(15,0)))
                .organizationId(organization)
                .build();
        HoursOfOperation hoursOfOperation4 = HoursOfOperation.builder()
                .day_of_the_week("friday")
                .opening_time(Time.valueOf(LocalTime.of(8,0)))
                .closing_time(Time.valueOf(LocalTime.of(15,0)))
                .organizationId(organization)
                .build();
        this.hoursOfOperationDAO.saveOrganizationsHoursOfOperation(hoursOfOperation);
        this.hoursOfOperationDAO.saveOrganizationsHoursOfOperation(hoursOfOperation1);
        this.hoursOfOperationDAO.saveOrganizationsHoursOfOperation(hoursOfOperation2);
        this.hoursOfOperationDAO.saveOrganizationsHoursOfOperation(hoursOfOperation3);
        this.hoursOfOperationDAO.saveOrganizationsHoursOfOperation(hoursOfOperation4);
    }
}
