package com.mealmatch.mealmatchapi.dao;

import com.mealmatch.mealmatchapi.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users;
    }
    @Transactional
    public User saveNewUser(User newUser) {
        return this.userRepository.save(newUser);
    }
}
