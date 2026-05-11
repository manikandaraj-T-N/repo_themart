package com.themart.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.themart.dto.RegisterRequest;
import com.themart.model.Product;
import com.themart.model.User;
import com.themart.repository.CategoryRepository;
import com.themart.repository.ProductRepository;
import com.themart.repository.UserRepository;
import com.themart.service.AuthService;
import com.themart.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final AuthService authService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

   @GetMapping("/home")
@Transactional(readOnly = true)
public String homePage(Model model,
                       @AuthenticationPrincipal UserDetails userDetails) {

    Pageable top12 = PageRequest.of(0, 12, Sort.by(Sort.Direction.DESC, "createdAt"));
    List<Product> newProducts = productRepository.findAll(top12).getContent();

    model.addAttribute("newProducts", newProducts);
    model.addAttribute("products", productRepository.findByIsFeaturedTrue());
    model.addAttribute("categories", categoryRepository.findAll());
    model.addAttribute("featuredProducts", productRepository.findByIsFeaturedTrue());
    model.addAttribute("dealProduct", productRepository.findById(419L).orElse(null));
    model.addAttribute("currentUri", "/home"); 

    if (userDetails != null) {
        model.addAttribute("cartCount", cartService.getCartCount(userDetails.getUsername()));
    } else {
        model.addAttribute("cartCount", 0);
    }

    return "home";
}

    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String email,
                    Model model) {

          model.addAttribute("email", email);
        return "login";
    }

@GetMapping("/register")
public String register(
        @RequestParam(required = false) Boolean google,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String name,
        Model model) {

    RegisterRequest request = new RegisterRequest();

    if (Boolean.TRUE.equals(google)) {
        request.setEmail(email);
        request.setName(name);
        model.addAttribute("isgoogleUser", true); 
    }

    model.addAttribute("registerRequest", request);
    return "register";
}

@PostMapping("/register")
public String registerSubmit(
        @Valid @ModelAttribute RegisterRequest registerRequest,
        BindingResult bindingResult,
        @RequestParam(required = false) Boolean isGoogleUser,
        Model model) {

    if (bindingResult.hasErrors()) {
        model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
        model.addAttribute("registerRequest", registerRequest);
        model.addAttribute("isGoogleUser", isGoogleUser);
        return "register";
    }

    User existingUser = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);


    if (existingUser != null && !Boolean.TRUE.equals(isGoogleUser)) {
        model.addAttribute("error", "Email already exists. Please login.");
        model.addAttribute("registerRequest", registerRequest);
        return "register";
    }

    try {
        User user;

        // ✅ GOOGLE USER → UPDATE EXISTING
        if (Boolean.TRUE.equals(isGoogleUser)) {
            user = existingUser;  // ⚠️ VERY IMPORTANT
        } 
        // ✅ NORMAL USER → CREATE NEW
        else {
            user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setName(registerRequest.getName());
            user.setProvider("LOCAL");
            user.setIsActive(true);
            user.setRole(User.Role.USER);
        }

        // ✅ Common fields
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPasswordSet(true);

        userRepository.save(user);

       return "redirect:/login?registered&email=" +
       URLEncoder.encode(registerRequest.getEmail(), StandardCharsets.UTF_8);

    } catch (Exception e) {
        model.addAttribute("error", "Registration failed.");
        model.addAttribute("registerRequest", registerRequest);
        model.addAttribute("isGoogleUser", isGoogleUser);
        return "register";
    }
}


    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("currentUri", "/contact"); 
        return "contact";
    }

    @GetMapping("/offers")
    public String offerPage(Model model) {
        model.addAttribute("currentUri", "/offers"); 
        return "offers";
    }

    @PostMapping("/contact/submit")
    public String contactSubmit(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phone,
                                @RequestParam(required = false) String subject,
                                @RequestParam String message,
                                Model model) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("manikandarajnatraj@gmail.com");
              mail.setSubject(subject != null && !subject.isBlank()
                        ? subject : "New Contact Message from " + name);
            mail.setText(
                "Name:    " + name    + "\n" +
                "Email:   " + email   + "\n" +
                "Phone:   " + phone   + "\n\n" +
                "Message:\n" + message
            );
            mail.setReplyTo(email);
            mailSender.send(mail);
            model.addAttribute("successMessage",
                "Thank you " + name + "! Your message has been sent successfully.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Something went wrong. Please try again.");
        }
        return "contact";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("currentUri", "/about"); 
        return "about";
    }

    @GetMapping("/legal")
public String legalPage(Model model) {
    model.addAttribute("currentUri", "/legal");
    return "legal";
}

@GetMapping("/terms")
public String termsPage(Model model) {
    model.addAttribute("currentUri", "/terms");
    return "terms";
}

@GetMapping("/secure-payment")
public String securePaymentPage(Model model) {
    model.addAttribute("currentUri", "/secure-payment");
    return "secure-payment";
}

    @ModelAttribute("currentUser")
    public User currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;
        return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
    }
}