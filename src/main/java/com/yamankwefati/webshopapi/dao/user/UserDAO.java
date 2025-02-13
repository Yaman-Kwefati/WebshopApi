package com.yamankwefati.webshopapi.dao.user;

import com.yamankwefati.webshopapi.model.User;
import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAO {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDAO(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public void saveNewUser(User newUser) {
        this.userRepository.save(newUser);
    }

    public Optional<User> getUserById(Long userId) {
        return this.userRepository.findById(userId);
    }

    public User updateUser(User updatedUser, Long userId) throws NotFoundException {
        Optional<User> oldUser = this.userRepository.findById(userId);
        if (oldUser.isEmpty()){
            throw new NotFoundException("User with id: " + userId + " not found");
        }
        User user = oldUser.get();
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());
        user.setPassword(updatedUser.getPassword());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setCity(updatedUser.getCity());
        user.setStreet(updatedUser.getStreet());
        user.setPostalCode(updatedUser.getPostalCode());
        return this.userRepository.save(user);
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String userEmail){
        return this.userRepository.findByEmail(userEmail);
    }

    @Transactional
    public void enableUser(Long userId) throws NotFoundException {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new NotFoundException("User with id: " + userId + " not found");
        }
        User user = userOptional.get();
        user.setEnabled(true);
        this.userRepository.save(user);
    }
}
