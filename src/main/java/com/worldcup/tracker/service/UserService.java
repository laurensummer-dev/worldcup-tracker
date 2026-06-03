package com.worldcup.tracker.service;

import com.worldcup.tracker.model.User;
import com.worldcup.tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String password){

        if (userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Username already taken.");
        }
        
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException(
                "Username must be between 3 and 50 characters");
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException(
                "Username can only contain letters, numbers and underscores");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        
        userRepository.save(user);
    }

    public boolean usernameExists(String username){
        return userRepository.existsByUsername(username);
    }
}
