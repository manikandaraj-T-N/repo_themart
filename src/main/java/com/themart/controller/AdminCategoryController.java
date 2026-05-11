package com.themart.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.themart.model.Category;
import com.themart.repository.CategoryRepository;
import com.themart.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryRepository categoryRepo;
       @Autowired
    private CategoryService categoryService;



    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/categories";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        model.addAttribute("category", category);
        return "admin/category-form";
    }

 @PostMapping("/save")
public String saveCategory(@ModelAttribute Category category,
                           @RequestParam("imageFile") MultipartFile imageFile) {

    // ✅ Save to images/categories/ (not uploads/categories/)
    if (imageFile != null && !imageFile.isEmpty()) {
        try {
            String uploadPath = System.getProperty("user.dir") + "/images/categories/";
            new java.io.File(uploadPath).mkdirs();

            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(uploadPath + filename);
            Files.write(path, imageFile.getBytes());

            // ✅ URL must match WebMvcConfig handler
            category.setImageUrl("/images/categories/" + filename);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    categoryService.save(category);
    return "redirect:/admin/categories";
}


    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
        return "redirect:/admin/categories";
    }
}