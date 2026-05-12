package com.themart.controller;

import java.io.IOException;
import java.util.List;

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
import com.themart.service.CloudinaryService;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired private ProductRepository productRepo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private CloudinaryService cloudinaryService; // ✅ ADD

    // ── List all products ──────────────────────────────────
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin/products";
    }

    // ── Show Add form ──────────────────────────────────────
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin/product-form";
    }

    // ── Show Edit form ─────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepo.findAll());

        return "admin/product-form";
    }

    // ── Save (Add or Update) ───────────────────────────────
    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "isFeatured", required = false) Boolean isFeatured,
            @RequestParam(value = "isOrganic", required = false) Boolean isOrganic,
            @RequestParam(value = "isActive", required = false) Boolean isActive
    ) throws IOException {

        // ── Checkboxes ─────────────────────────────
        product.setIsFeatured(isFeatured != null);
        product.setIsOrganic(isOrganic != null);
        product.setIsActive(isActive != null);

        // ── Discount null safe ────────────────────
        if (product.getDiscountPercentage() == null) {
            product.setDiscountPercentage(0);
        }

        // ── Stock null safe ───────────────────────
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }

        // ── Category + Gender ─────────────────────
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

        // ── Gender fallback ───────────────────────
        if (product.getGender() == null || product.getGender().isBlank()) {
            product.setGender("unisex");
        }

        // ── Cloudinary Image Upload ───────────────
        if (imageFile != null && !imageFile.isEmpty()) {

            // Validate size
            if (imageFile.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("Image must be under 5MB.");
            }

            // Validate extension
            String originalName = imageFile.getOriginalFilename();

            String ext = originalName != null
                    ? originalName.substring(originalName.lastIndexOf('.')).toLowerCase()
                    : ".jpg";

            if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext)) {
                throw new IllegalArgumentException(
                        "Only JPG, PNG, or WEBP images are allowed."
                );
            }

            // Delete old image when editing
            if (product.getId() != null) {
                productRepo.findById(product.getId()).ifPresent(existing -> {
                     if (existing.getImageUrl() != null &&
                !existing.getImageUrl().isBlank()) {
            cloudinaryService.deleteImage(existing.getImageUrl()); // no try-catch needed
        }
                });
            }

            // Upload new image
            String imageUrl = cloudinaryService.uploadImage(imageFile);

            // Save URL
            product.setImageUrl(imageUrl);
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
   // Delete product
@PostMapping("/delete/{id}")
public String deleteProduct(@PathVariable Long id) {
    productRepo.findById(id).ifPresent(product -> {
        if (product.getImageUrl() != null &&
                !product.getImageUrl().isBlank()) {
            cloudinaryService.deleteImage(product.getImageUrl()); // no try-catch needed
        }
        productRepo.delete(product);
    });
    return "redirect:/admin/products";
}

}