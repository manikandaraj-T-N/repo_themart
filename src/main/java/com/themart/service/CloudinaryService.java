package com.themart.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

public String uploadImage(MultipartFile file) throws IOException {
    Map<String, Object> options = new HashMap<>();
    options.put("folder", "themart/products"); // ← was "themart"
    
    Map<String, Object> uploadResult = cloudinary.uploader().upload(
        file.getBytes(), options
    );
    return (String) uploadResult.get("secure_url");
}

// Add separate method for profile images
public String uploadProfileImage(MultipartFile file) throws IOException {
    Map<String, Object> options = new HashMap<>();
    options.put("folder", "themart/profiles"); // ← profiles folder
    
    Map<String, Object> uploadResult = cloudinary.uploader().upload(
        file.getBytes(), options
    );
    return (String) uploadResult.get("secure_url");
}

// Add separate method for category images
public String uploadCategoryImage(MultipartFile file) throws IOException {
    Map<String, Object> options = new HashMap<>();
    options.put("folder", "themart/categories");
    
    Map<String, Object> uploadResult = cloudinary.uploader().upload(
        file.getBytes(), options
    );
    return (String) uploadResult.get("secure_url");
}


private String extractPublicId(String imageUrl) {
    // Handles: /upload/v123/themart/products/filename.jpg
    // OR:      /upload/themart/products/filename.jpg
    try {
        String[] parts = imageUrl.split("/upload/");
        String afterUpload = parts[1];
        // Remove version if present (v1234567/)
        if (afterUpload.startsWith("v") && afterUpload.contains("/")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
        }
        // Remove file extension
        return afterUpload.substring(0, afterUpload.lastIndexOf("."));
    } catch (Exception e) {
        return imageUrl;
    }
}

}