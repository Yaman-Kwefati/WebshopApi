package com.yamankwefati.webshopapi.dao.product;

import com.yamankwefati.webshopapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getByName(String name);
}
