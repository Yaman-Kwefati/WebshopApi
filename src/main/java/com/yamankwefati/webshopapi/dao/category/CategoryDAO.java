package com.yamankwefati.webshopapi.dao.category;

import com.yamankwefati.webshopapi.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDAO {

    private final CategoryRepository categoryRepository;

    public CategoryDAO(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories(){
        return this.categoryRepository.findAll();
    }

    public Category getCategoryById(String name){
        return this.categoryRepository.getByCategoryName(name);
    }

    public Category addCategory(Category category){
        return this.categoryRepository.save(category);
    }

    public void removeCategory(Long id){
        Optional<Category> category = this.categoryRepository.findById(id);
        this.categoryRepository.delete(category.get());
    }
}
