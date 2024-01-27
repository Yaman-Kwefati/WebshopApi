package com.yamankwefati.webshopapi.seeder;

import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {
    @Autowired
    UserSeeder userSeeder;
    @Autowired
    ProductSeeder productSeeder;
    private boolean alreadySeeded = false;


    @EventListener
    public void seed(ContextRefreshedEvent event){
        if(alreadySeeded){
            return;
        }
        userSeeder.seed();
        productSeeder.seed();
        this.alreadySeeded = true;
    }
}
