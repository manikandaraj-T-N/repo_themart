package com.themart.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.themart.model.Product;
import com.themart.repository.CategoryRepository;
import com.themart.repository.ProductRepository;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired private ProductRepository  productRepo;
    @Autowired private CategoryRepository categoryRepo;

    // ── List all products ──────────────────────────────────
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin/products";          // templates/admin/products.html
    }

    // ── Show Add form ──────────────────────────────────────
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("product",    new Product());
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/product-form";      // templates/admin/product-form.html
    }

    // ── Show Edit form ─────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product",    product);
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/product-form";
    }

    // ── Save (Add or Update) ───────────────────────────────
@PostMapping("/save")
public String saveProduct(
        @ModelAttribute Product product,
        @RequestParam(value = "imageFile",  required = false) MultipartFile imageFile,
        @RequestParam(value = "categoryId", required = false) Long categoryId,
        @RequestParam(value = "isFeatured", required = false) Boolean isFeatured,
        @RequestParam(value = "isOrganic",  required = false) Boolean isOrganic,
        @RequestParam(value = "isActive",   required = false) Boolean isActive) throws IOException {

    // ── Checkboxes (null when unchecked) ──
    product.setIsFeatured(isFeatured != null);
    product.setIsOrganic(isOrganic   != null);
    product.setIsActive(isActive     != null);

    // ── Discount null safe ──
    if (product.getDiscountPercentage() == null) {
        product.setDiscountPercentage(0);
    }

    // ── Stock null safe ──
    if (product.getStockQuantity() == null) {
        product.setStockQuantity(0);
    }

    // ── Category + Auto-set gender from category ──        // ← REPLACED
    if (categoryId != null) {
        categoryRepo.findById(categoryId).ifPresent(cat -> {
            product.setCategory(cat);
            if (cat.getGender() != null && !cat.getGender().isBlank()) {
                product.setGender(cat.getGender());
            } else {
                product.setGender("unisex");
            }
        });
    }

    // ── Gender fallback (if no category or category has no gender) ──
    if (product.getGender() == null || product.getGender().isBlank()) {
        product.setGender("unisex");
    }

    // ── Image Upload ──
    if (imageFile != null && !imageFile.isEmpty()) {

        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Image must be under 5MB.");
        }

        String originalName = imageFile.getOriginalFilename();
        String ext = originalName != null
                ? originalName.substring(originalName.lastIndexOf('.')).toLowerCase()
                : ".jpg";
        if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext)) {
            throw new IllegalArgumentException("Only JPG, PNG, or WEBP images are allowed.");
        }

        String filename   = UUID.randomUUID() + "_" + originalName;
        Path   uploadPath = Paths.get("images/products").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        Files.copy(imageFile.getInputStream(),
                   uploadPath.resolve(filename),
                   StandardCopyOption.REPLACE_EXISTING);
        product.setImageUrl("/images/products/" + filename);
    }

    productRepo.save(product);
    return "redirect:/admin/products";
}

    // ── Toggle Featured ────────────────────────────────────
    @PostMapping("/toggle-featured/{id}")
    public String toggleFeatured(@PathVariable Long id) {
        productRepo.findById(id).ifPresent(p -> {
            p.setIsFeatured(!Boolean.TRUE.equals(p.getIsFeatured()));
            productRepo.save(p);
        });
        return "redirect:/admin/products";
    }

    // ── Delete ─────────────────────────────────────────────
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/admin/products";
    }
}