package com.mealmatch.mealmatchapi.dao;

import com.mealmatch.mealmatchapi.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAO {
    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public void saveNewUser(User newUser) {
        this.userRepository.save(newUser);
    }

    public Optional<User> getUserById(Long userId){
        return Optional.of(this.userRepository.findById(userId).get());
    }
}
