package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.product.ProductDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.Product;
import com.yamankwefati.webshopapi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all-products")
    @ResponseBody
    public ApiResponse<List<Product>> getAllProducts(){
        try {
            List<Product> products = this.productDAO.getAllProducts();
            return new ApiResponse<>(HttpStatus.ACCEPTED, products);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all products");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{productName}")
    @ResponseBody
    public ApiResponse<Product> getProduct(@PathVariable String productName) {
        try {
            Product product = this.productDAO.getProductByName(productName);
            return new ApiResponse<>(HttpStatus.ACCEPTED, product);
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
