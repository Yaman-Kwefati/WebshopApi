package com.yamankwefati.webshopapi.dao.category;

import com.yamankwefati.webshopapi.dao.product.ProductRepository;
import com.yamankwefati.webshopapi.model.Category;
import com.yamankwefati.webshopapi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDAO {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryDAO(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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

    @Transactional
    public void addProductToCategory(Long categoryId, Long productId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        category.getProductList().add(product);
        product.getCategoryList().add(category);

        // Save the changes
        categoryRepository.save(category);
    }
}
