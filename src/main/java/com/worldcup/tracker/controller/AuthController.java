package com.worldcup.tracker.controller;

import com.worldcup.tracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    // Show login page
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value="error", required=false) String error,
            @RequestParam(value="logout", required=false) String logout,
            Model model) {
            
        if (null != error) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }

        if (null != logout) {
            model.addAttribute("successMessage", "You have been logged out.");
        }

        return "auth/login";
    }

    // Show registration page
    @GetMapping("/register")
    public String registerPage(){
        return "auth/register";
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model){

        if (!password.equals(confirmPassword)){
            model.addAttribute("errorMessage", "Passwords do not match");
            return "auth/register";
        }

        if (password.length() < 8){
            model.addAttribute("errorMessage", "Password must be atleast 8 characters long");
            return "auth/register";
        }

        try {
            userService.registerUser(username, confirmPassword);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }
}
