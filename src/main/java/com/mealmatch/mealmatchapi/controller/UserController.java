package com.mealmatch.mealmatchapi.controller;
import com.mealmatch.mealmatchapi.dao.UserDAO;
import com.mealmatch.mealmatchapi.model.ApiResponse;
import com.mealmatch.mealmatchapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/users")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(method = RequestMethod.GET )
    @ResponseBody
    ApiResponse<List<User>> all(){
        try {
            List<User> users = this.userDAO.getAllUsers();
            return new ApiResponse<>(HttpStatus.ACCEPTED, users);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "Could not fetch all users");
        }
    }
}
