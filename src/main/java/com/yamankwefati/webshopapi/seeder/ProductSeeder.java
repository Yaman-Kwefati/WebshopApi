package com.yamankwefati.webshopapi.seeder;

import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.model.Product;
import com.yamankwefati.webshopapi.model.Role;
import com.yamankwefati.webshopapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductSeeder {
    @Autowired
    private ProductDAO productDAO;
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
        Product product = Product.builder()
                .id(1L)
                .name("Parfum 1")
                .description("250ML")
                .price(100.00)
                .images(List.of("Capture-2024-01-12-192550.png"))
                .stockQuantity(100)
                .build();
        this.productDAO.addNewProduct(product);
        Product product2 = Product.builder()
                .id(2L)
                .name("Parfum 2")
                .description("250ML")
                .price(140.00)
                .images(List.of("Capture-2024-01-12-192550.png"))
                .stockQuantity(100)
                .build();
        this.productDAO.addNewProduct(product2);
        Product product3 = Product.builder()
                .id(3L)
                .name("Parfum 3")
                .description("250ML")
                .price(120.00)
                .images(List.of("Capture-2024-01-12-192550.png"))
                .stockQuantity(100)
                .build();
        this.productDAO.addNewProduct(product3);
    }
}
