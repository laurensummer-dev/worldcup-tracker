package com.worldcup.tracker.controller;

import com.worldcup.tracker.model.User;
import com.worldcup.tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class BaseController {

    protected final UserRepository userRepository;

    public BaseController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getUser(UserDetails userDetails) {
        return userRepository.findByUsername(
                userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException(
                        "Logged in user not found in database"));
    }
}