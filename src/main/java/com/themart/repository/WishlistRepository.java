package com.themart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.themart.model.Product;
import com.themart.model.User;
import com.themart.model.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    boolean existsByUserAndProduct(User user, Product product);
    int countByUser(User user);
    long countByUserEmail(String username);
} 