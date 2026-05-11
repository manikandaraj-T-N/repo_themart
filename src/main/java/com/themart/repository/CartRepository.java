package com.themart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.themart.model.CartItem;
import com.themart.model.Category;
import com.themart.model.Product;
import com.themart.model.User;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    void deleteByUser(User user);
    int countByUser(User user);

    //  Optional<Category> findBySlug(String slug);
}