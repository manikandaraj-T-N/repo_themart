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
    options.put("folder", "themart");
    
    Map<String, Object> uploadResult = cloudinary.uploader().upload(
        file.getBytes(), options
    );
    return (String) uploadResult.get("secure_url");
}

public void deleteImage(String imageUrl) throws IOException {
    String publicId = extractPublicId(imageUrl);
    cloudinary.uploader().destroy(publicId, new HashMap<>());
}

    private String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String filename = parts[parts.length - 1];
        String folder = parts[parts.length - 2];
        return folder + "/" + filename.split("\\.")[0];
    }
}