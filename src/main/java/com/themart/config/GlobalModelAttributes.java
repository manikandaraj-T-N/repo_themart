package com.themart.config;

import com.themart.model.User;
import com.themart.repository.UserRepository;
import com.themart.repository.WishlistRepository;
import com.themart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final CartService cartService;
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    @ModelAttribute
    public void addGlobalAttributes(
            @AuthenticationPrincipal Object principal,
            Model model) {

        String email = null;

        // Handle both form login and OAuth2 login
        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
        }

        if (email != null) {
            try {
                int cartCount = cartService.getCartCount(email);
                model.addAttribute("cartCount", cartCount);

                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    model.addAttribute("wishlistCount",
                        wishlistRepository.countByUser(user));
                    // ← Add currentUser here so header can show the name/email
                    model.addAttribute("currentUser", user);
                } else {
                    model.addAttribute("wishlistCount", 0);
                }

            } catch (Exception e) {
                System.err.println("GlobalModelAttributes ERROR: " + e.getMessage());
                model.addAttribute("cartCount", 0);
                model.addAttribute("wishlistCount", 0);
            }
        } else {
            model.addAttribute("cartCount", 0);
            model.addAttribute("wishlistCount", 0);
        }
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}