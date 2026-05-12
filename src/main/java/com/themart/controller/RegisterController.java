
package com.themart.controller;

import com.themart.dto.RegisterRequest;
import com.themart.repository.UserRepository;
import com.themart.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest, Model model) {

        // Check duplicate email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            model.addAttribute("error", "An account with this email already exists. Please login instead.");
            model.addAttribute("registerRequest", registerRequest);
            return "register";
        }

        try {
            authService.register(registerRequest);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            model.addAttribute("registerRequest", registerRequest);
            return "register";
        }
    }
}