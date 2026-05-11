package com.themart.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.themart.model.Blog;
import com.themart.repository.BlogRepository;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogRepository blogRepo;

    // Blog listing page
    @GetMapping
    public String blogList(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        List<Blog> blogs;

        if (category != null && !category.isBlank()) {
            blogs = blogRepo.findByIsPublishedTrueAndCategoryOrderByCreatedAtDesc(category);
        } else {
            blogs = blogRepo.findByIsPublishedTrueOrderByCreatedAtDesc();
        }

        // Latest 3 for hero section
        List<Blog> latestBlogs = blogs.stream().limit(3).collect(Collectors.toList());

        // Paginate remaining
        int pageSize   = 6;
        int fromIndex  = page * pageSize;
        int toIndex    = Math.min(fromIndex + pageSize, blogs.size());
        List<Blog> pagedBlogs = fromIndex < blogs.size()
                                ? blogs.subList(fromIndex, toIndex)
                                : List.of();

        // All unique categories
        List<String> categories = blogRepo.findByIsPublishedTrueOrderByCreatedAtDesc()
                .stream()
                .map(Blog::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("latestBlogs",  latestBlogs);
        model.addAttribute("blogs",        pagedBlogs);
        model.addAttribute("categories",   categories);
        model.addAttribute("currentCat",   category);
        model.addAttribute("currentPage",  page);
        model.addAttribute("totalPages",   (int) Math.ceil((double) blogs.size() / pageSize));
        return "blog";
    }

    // Blog detail page
    @GetMapping("/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        Blog blog = blogRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));

        // Related blogs (same category, exclude current)
        List<Blog> related = blogRepo
            .findByIsPublishedTrueAndCategoryOrderByCreatedAtDesc(blog.getCategory())
            .stream()
            .filter(b -> !b.getId().equals(id))
            .limit(3)
            .collect(Collectors.toList());

        model.addAttribute("blog",    blog);
        model.addAttribute("related", related);
        return "blog-detail";
    }
}
