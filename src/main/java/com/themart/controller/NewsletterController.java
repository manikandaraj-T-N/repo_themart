package com.themart.controller;

import com.themart.model.NewsletterSubscriber;
import com.themart.repository.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterSubscriberRepository subscriberRepository;

    @PostMapping("/newsletter/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestParam String email) {

        if (email == null || email.isBlank() || !email.contains("@")) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "Please enter a valid email address."));
        }

        if (subscriberRepository.existsByEmail(email.trim().toLowerCase())) {
            return ResponseEntity.ok()
                .body(Map.of("success", false, "message", "You are already subscribed!"));
        }

        subscriberRepository.save(
            NewsletterSubscriber.builder()
                .email(email.trim().toLowerCase())
                .build()
        );

        return ResponseEntity.ok()
            .body(Map.of("success", true, "message", "Thank you for subscribing!"));
    }
}