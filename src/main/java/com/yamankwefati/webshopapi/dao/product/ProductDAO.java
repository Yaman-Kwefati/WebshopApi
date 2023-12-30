package com.yamankwefati.webshopapi.dao.product;

import com.yamankwefati.webshopapi.model.Product;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDAO {
    private final ProductRepository productRepository;

    public ProductDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }

    public Product getProductByName(String productName) {
        return productRepository.getByName(productName);
    }

    public Product addNewProduct(Product product){
        return this.productRepository.save(product);
    }

    public Product updateProduct(Product product, Long oldId) throws NotFoundException {
        Optional<Product> optionalProduct = this.productRepository.findById(oldId);
        if (optionalProduct.isEmpty()){
            throw new NotFoundException("User with id: " + oldId + " not found");
        }
        Product product1 = optionalProduct.get();
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setPrice(product.getPrice());
        product1.setStockQuantity(product.getStockQuantity());
        product1.setCategoryList(product.getCategoryList());
        return this.productRepository.save(product1);
    }

    public void deleteProduct(Long productId) throws NotFoundException {
        Optional<Product> product = this.productRepository.findById(productId);
        if (product.isEmpty()){
            throw new NotFoundException("User with id: " + productId + " not found");
        }
        this.productRepository.delete(product.get());
    }
}
