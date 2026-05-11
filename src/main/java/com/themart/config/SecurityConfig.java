package com.themart.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.themart.repository.UserRepository;
import com.themart.service.CustomOAuth2UserService;
import com.themart.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity 
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder); // ← fix deprecated
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(
                    "/", "/home", "/products", "/category/**",
                    "/css/**", "/js/**", "/images/**",
                    "/login", "/register",
                    "/contact", "/contact/submit",
                    "/about", "/blog", "/search",
                    "/blog/**", "/shop", "/shop/**",
                    "/faq", "/terms", "/privacy",
                    "/newsletter/subscribe",
                    "/legal","/secure-payment"
                ).permitAll()
                .requestMatchers("/images/profiles/**").permitAll()
                .requestMatchers(
                    "/cart/**", "/checkout", "/checkout/**",
                    "/orders/**", "/order/**", "/payment/**",
                    "/profile", "/wishlist/**",
                    "/my-orders/**", "/orders"
                ).authenticated()

                .requestMatchers("/set-password").authenticated()
                
                .anyRequest().permitAll()
            )

            .oauth2Login(oauth2 -> oauth2
            .loginPage("/login")
            .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
            // .defaultSuccessUrl("/home", true)
            .successHandler(oAuth2SuccessHandler()) 
        )

        .formLogin(form -> form
            .loginPage("/login")
            .successHandler(customSuccessHandler())
            .failureUrl("/login?error=true")
            .permitAll()
        )

            .csrf(csrf -> csrf.disable())
            .logout(logout -> logout
                .logoutSuccessUrl("/")
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                response.sendRedirect("/admin/dashboard");
            } else {
                response.sendRedirect("/home");
            }
        };
    }

    @Bean
public AuthenticationSuccessHandler oAuth2SuccessHandler() {
    return (request, response, authentication) -> {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        userRepository.findByEmail(email).ifPresent(user -> {
            try {
                
                if ("GOOGLE".equals(user.getProvider()) && Boolean.FALSE.equals(user.getPasswordSet())) {
                 String redirectUrl = "/register?google=true"
                + "&email=" + user.getEmail()
                + "&name=" + user.getName();

    response.sendRedirect(redirectUrl);
}
                else if (user.getPhone() == null || user.getPhone().isBlank()) {
                    response.sendRedirect("/profile?newUser=true");
                } 
                else {
                    response.sendRedirect("/home");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    };
}

}