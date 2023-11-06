package com.mealmatch.mealmatchapi.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {
    @Autowired
    UserSeeder userSeeder;
    @Autowired
    OrganizationSeeder organizationSeeder;
    private boolean alreadySeeded = false;


    @EventListener
    public void seed(ContextRefreshedEvent event){
        if(alreadySeeded){
            return;
        }
        organizationSeeder.seed();
        userSeeder.seed();
        this.alreadySeeded = true;
    }
}
