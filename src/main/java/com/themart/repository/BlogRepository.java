package com.themart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.themart.model.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByIsPublishedTrueOrderByCreatedAtDesc();
    List<Blog> findByIsPublishedTrueAndCategoryOrderByCreatedAtDesc(String category);
    Page<Blog> findByIsPublishedTrue(Pageable pageable);
}