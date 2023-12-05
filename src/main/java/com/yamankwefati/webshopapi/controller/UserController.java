package com.yamankwefati.webshopapi.controller;
import com.yamankwefati.webshopapi.dao.user.UserDAO;
import com.yamankwefati.webshopapi.model.ApiResponse;
import com.yamankwefati.webshopapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/api/v1/users")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    //Get all users
    @RequestMapping(method = RequestMethod.GET, value = "/all-users")
    @ResponseBody
    ApiResponse<List<User>> getAllUsers(){
        try {
            List<User> users = this.userDAO.getAllUsers();
            return new ApiResponse<>(HttpStatus.ACCEPTED, users);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all users");
        }
    }

    //Get a user by id
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    @ResponseBody
    ApiResponse<Optional<User>> getUserById(@PathVariable Long userId) {

        try {
            Optional<User> user = this.userDAO.getUserById(userId);
            return new ApiResponse<>(HttpStatus.ACCEPTED, user);
        } catch (Exception e){
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
