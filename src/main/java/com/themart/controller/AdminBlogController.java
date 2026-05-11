package com.themart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.themart.model.Blog;
import com.themart.repository.BlogRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/blogs")
public class AdminBlogController {

    @Autowired
    private BlogRepository blogRepo;

    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogRepo.findAll(
            Sort.by(Sort.Direction.DESC, "createdAt")));
        return "admin/blogs";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "admin/blog-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Blog blog = blogRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "admin/blog-form";
    }

    @PostMapping("/save")
    public String saveBlog(
            @ModelAttribute Blog blog,
            @RequestParam(value = "isPublished", required = false) Boolean isPublished) {
        blog.setIsPublished(isPublished != null);
        blogRepo.save(blog);
        return "redirect:/admin/blogs";
    }

    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogRepo.deleteById(id);
        return "redirect:/admin/blogs";
    }
}