package com.themart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.themart.model.NewsletterSubscriber;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {
    boolean existsByEmail(String email);
}