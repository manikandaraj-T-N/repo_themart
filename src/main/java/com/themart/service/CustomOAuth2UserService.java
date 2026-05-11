package com.themart.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.themart.model.User;
import com.themart.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPassword(null);
            newUser.setProvider("GOOGLE");
            newUser.setPasswordSet(false);
            newUser.setRole(User.Role.USER);
            newUser.setIsActive(true);
            return userRepository.save(newUser);
        });

        // ✅ Update name if changed in Google
        if (name != null && !name.equals(user.getName())) {
            user.setName(name);
        }

        // ✅ Ensure provider is GOOGLE
        if (!"GOOGLE".equals(user.getProvider())) {
            user.setProvider("GOOGLE");
        }

        userRepository.save(user);

        return oAuth2User;
    }
}