package com.themart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.themart.model.Product;
import com.themart.service.ProductService;
import com.themart.service.CloudinaryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    // ⚠️ @Value fields must NOT be final — @RequiredArgsConstructor only injects final fields
    @Value("${app.upload.dir}")
    private String uploadDir;  // → "images/products"

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    // ── GET all products ──────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllActiveProducts());
    }

    // ── GET single product ────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ── POST upload product image ─────────────────────────────
    @PostMapping("/admin/upload-image")
    public ResponseEntity<Map<String, Object>> uploadProductImage(
            @RequestParam("file") MultipartFile file) {
        try {
            String uploadPath = System.getProperty("user.dir") + "/" + uploadDir + "/";
            new File(uploadPath).mkdirs();

            String filename = "product_" + System.currentTimeMillis()
                    + getExtension(file.getOriginalFilename());

            Path path = Paths.get(uploadPath + filename);
            Files.write(path, file.getBytes());

            // String imageUrl = "/images/products/" + filename;
            String imageUrl = cloudinaryService.uploadImage(file);

            return ResponseEntity.ok(Map.of("success", true, "imageUrl", imageUrl));

        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // ── POST create product ───────────────────────────────────
    @PostMapping("/admin")
    public ResponseEntity<Product> createProduct(@Validated @RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    // ── PUT update product ────────────────────────────────────
    @PutMapping("/admin/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                  @Validated @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    // ── DELETE product ────────────────────────────────────────
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }

    // ── Helper ────────────────────────────────────────────────
    private String getExtension(String filename) {
        if (filename == null) return ".jpg";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".jpg";
    }
}