package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.Product;
import com.yamankwefati.webshopapi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/v1/products")
public class ProductController {
    private final ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all-products")
    @ResponseBody
    public ApiResponse<List<Product>> getAllProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        try {
            List<Product> products = this.productDAO.getAllProducts();
            if (!isAdmin) {
                products.forEach(product -> product.setStockQuantity(0));
            }
            return new ApiResponse<>(HttpStatus.ACCEPTED, products);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all products");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/name/{productName}")
    @ResponseBody
    public ApiResponse<Product> getProduct(@PathVariable String productName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        try {
            Product product = this.productDAO.getProductByName(productName);
            if (!isAdmin) {
                product.setStockQuantity(0);
            }
            return new ApiResponse<>(HttpStatus.ACCEPTED, product);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Product doesn't Exist!");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/id/{productId}")
    @ResponseBody
    public ApiResponse<Product> getProductById(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));

        try {
            Optional<Product> optionalProduct = this.productDAO.getProductById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                if (!isAdmin) {
                    product.setStockQuantity(0);
                }
                return new ApiResponse<>(HttpStatus.ACCEPTED, product);
            } else {
                return new ApiResponse<>(HttpStatus.NOT_FOUND, "Product doesn't Exist!");
            }
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Product doesn't Exist!");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/new-product")
    @ResponseBody
    public ApiResponse<Product> addNewProduct(
            @RequestBody Product product
    ){
        try {
            Product newProduct = this.productDAO.addNewProduct(product);
            return new ApiResponse<>(HttpStatus.ACCEPTED, newProduct);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't add Product");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{productId}")
    @ResponseBody
    ApiResponse<Product> updateProduct(
            @RequestBody Product updatedProduct,
            @PathVariable Long productId) {
        try {
            Product product = this.productDAO.updateProduct(updatedProduct, productId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, product);
        } catch (Exception e){
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{productId}")
    @ResponseBody
    public ApiResponse<Product> deleteProduct(@PathVariable Long productId){
        try {
            this.productDAO.deleteProduct(productId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, "Product Deleted");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't find Product");
        }
    }
}
