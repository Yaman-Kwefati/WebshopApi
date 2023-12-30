package com.yamankwefati.webshopapi.dao.category;

import com.yamankwefati.webshopapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getByCategoryName(String Name);
}
