package com.themart.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.themart.model.Product;
import com.themart.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ProductRepository productRepository;

 @GetMapping("/search")
public String search(@RequestParam(required = false) String q) {
    if (q != null && !q.isBlank()) {
        return "redirect:/shop?search=" + q.trim();
    }
    return "redirect:/shop";
}

}