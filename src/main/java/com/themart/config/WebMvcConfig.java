package com.themart.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

   @Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {

    // ✅ images/products
    Path productsRoot = Paths.get("images/products").toAbsolutePath().normalize();
    registry.addResourceHandler("/images/products/**")
            .addResourceLocations("file:" + productsRoot.toString() + "/");

    // ✅ images/profiles
    Path profilesRoot = Paths.get("images/profiles").toAbsolutePath().normalize();
    registry.addResourceHandler("/images/profiles/**")
            .addResourceLocations("file:" + profilesRoot.toString() + "/");

            // ✅ Category images — ADD THIS
    Path categoriesRoot = Paths.get("images/categories").toAbsolutePath().normalize();
    registry.addResourceHandler("/images/categories/**")
            .addResourceLocations("file:" + categoriesRoot.toString() + "/");
}

}