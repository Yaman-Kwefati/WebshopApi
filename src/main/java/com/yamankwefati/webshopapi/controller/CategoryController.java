package com.yamankwefati.webshopapi.controller;

import com.yamankwefati.webshopapi.dao.category.CategoryDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.Category;
import com.yamankwefati.webshopapi.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all-categories")
    @ResponseBody
    ApiResponse<List<Category>> getAllUsers(){
        try {
            List<Category> categories = this.categoryDAO.getAllCategories();
            return new ApiResponse<>(HttpStatus.ACCEPTED, categories);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all categories");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{categoryName}")
    @ResponseBody
    ApiResponse<Category> getCategory(@PathVariable String categoryName){
        try {
            Category categorie = this.categoryDAO.getCategoryById(categoryName);
            return new ApiResponse<>(HttpStatus.ACCEPTED, categorie);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Category doesn't Exist!");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/new-category")
    @ResponseBody
    public ApiResponse<Category> placeNewOrder(
            @RequestBody Category category
    ){
        try {
            Category category1 = this.categoryDAO.addCategory(category);
            return new ApiResponse<>(HttpStatus.ACCEPTED, category1);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't add Category");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{categoryId}")
    @ResponseBody
    public ApiResponse<Category> deleteCategory(@PathVariable Long categoryId){
        try {
            this.categoryDAO.removeCategory(categoryId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, "Category Deleted");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Couldn't find Category");
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{categoryId}/addProduct")
    @ResponseBody
    public ApiResponse<String> addProductToCategory(
            @PathVariable Long categoryId,
            @RequestBody Long productId) {

        try {
            this.categoryDAO.addProductToCategory(categoryId, productId);
            return new ApiResponse<>(HttpStatus.OK, "Product added to category successfully");
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
