package com.themart.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // Upload product image
    public String uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("folder", "themart/products");

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
            file.getBytes(), options
        );
        return (String) uploadResult.get("secure_url");
    }

    // Upload profile image
    public String uploadProfileImage(MultipartFile file) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("folder", "themart/profiles");

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
            file.getBytes(), options
        );
        return (String) uploadResult.get("secure_url");
    }

    // Upload category image
    public String uploadCategoryImage(MultipartFile file) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("folder", "themart/categories");

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
            file.getBytes(), options
        );
        return (String) uploadResult.get("secure_url");
    }

    // Delete image by URL
    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, new HashMap<>());
        } catch (Exception e) {
            System.err.println("Failed to delete image: " + e.getMessage());
        }
    }

    // Extract public_id from Cloudinary URL
    private String extractPublicId(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/upload/");
            String afterUpload = parts[1];
            // Remove version if present (v1234567/)
            if (afterUpload.startsWith("v") && afterUpload.matches("v\\d+/.*")) {
                afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
            }
            // Remove file extension
            return afterUpload.substring(0, afterUpload.lastIndexOf("."));
        } catch (Exception e) {
            return imageUrl;
        }
    }
}