package com.themart.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.themart.model.Product;
import com.themart.model.User;
import com.themart.repository.CategoryRepository;
import com.themart.repository.ProductRepository;
import com.themart.repository.UserRepository;
import com.themart.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

@GetMapping("/shop")
public String shop(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer rating,
        @RequestParam(required = false) String tag,
        @RequestParam(defaultValue = "latest") String sort,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model) {

    // ── Sanitize empty strings to null ─────────────────────
final String categoryF = (category != null && !category.isBlank()) ? category : null;
final String searchF   = (search   != null && !search.isBlank())   ? search   : null;
final String tagF      = (tag      != null && !tag.isBlank())       ? tag      : null;
final String genderF   = (gender   != null && !gender.isBlank())    ? gender   : null;
final String sortF     = (sort     != null && !sort.isBlank())      ? sort     : "latest";

List<Product> products = productRepository.findAll();

// ── Search filter ──────────────────────────────────────
if (searchF != null) {
    String keyword = searchF.toLowerCase().trim();
    products = products.stream()
        .filter(p ->
            (p.getName()             != null && p.getName().toLowerCase().contains(keyword)) ||
            (p.getDescription()      != null && p.getDescription().toLowerCase().contains(keyword)) ||
            (p.getTags()             != null && p.getTags().toLowerCase().contains(keyword)) ||
            (p.getShortDescription() != null && p.getShortDescription().toLowerCase().contains(keyword)) ||
            (p.getCategory()         != null && p.getCategory().getName().toLowerCase().contains(keyword))
        )
        .collect(Collectors.toList());
}

// ── Gender filter ──────────────────────────────────────
if (genderF != null) {
    products = products.stream()
        .filter(p -> genderF.equalsIgnoreCase(p.getGender()))
        .collect(Collectors.toList());
}

// ── Category slug filter ───────────────────────────────
if (categoryF != null && !categoryF.equals("all")) {
    products = products.stream()
        .filter(p -> p.getCategory() != null && (
            categoryF.equals(p.getCategory().getSlug()) ||
            (p.getCategory().getParent() != null &&
             categoryF.equals(p.getCategory().getParent().getSlug()))
        ))
        .collect(Collectors.toList());
}

// ── Category ID filter ─────────────────────────────────
if (categoryId != null) {
    products = products.stream()
        .filter(p -> p.getCategory() != null &&
                     p.getCategory().getId().equals(categoryId))
        .collect(Collectors.toList());
}

// ── Price filter ───────────────────────────────────────
if (maxPrice != null) {
    BigDecimal max = BigDecimal.valueOf(maxPrice);
    products = products.stream()
        .filter(p -> p.getPrice() != null &&
                     p.getPrice().compareTo(max) <= 0)
        .collect(Collectors.toList());
}

// ── Rating filter ──────────────────────────────────────
if (rating != null) {
    BigDecimal minRating = BigDecimal.valueOf(rating);
    products = products.stream()
        .filter(p -> p.getRating() != null &&
                     p.getRating().compareTo(minRating) >= 0)
        .collect(Collectors.toList());
}

// ── Tag filter ─────────────────────────────────────────
if (tagF != null) {
    products = products.stream()
        .filter(p -> p.getTags() != null &&
            Arrays.stream(p.getTags().split(","))
                  .map(String::trim)
                  .anyMatch(t -> t.equalsIgnoreCase(tagF)))
        .collect(Collectors.toList());
}

// ── Sort ───────────────────────────────────────────────
switch (sortF) {
    case "price-low"  -> products.sort(
        Comparator.comparing(p -> p.getPrice() == null ? BigDecimal.ZERO : p.getPrice())
    );
    case "price-high" -> products.sort(
        Comparator.comparing((Product p) ->
            p.getPrice() == null ? BigDecimal.ZERO : p.getPrice()
        ).reversed()
    );
    case "rating"     -> products.sort(
        Comparator.comparing((Product p) ->
            p.getRating() == null ? BigDecimal.ZERO : p.getRating()
        ).reversed()
    );
    case "latest"     -> products.sort(
        Comparator.comparing((Product p) ->
            p.getCreatedAt() == null ? LocalDateTime.MIN : p.getCreatedAt()
        ).reversed()
    );
}

// ── Pagination ─────────────────────────────────────────
int pageSize  = 12;
int total     = products.size();
int fromIndex = page * pageSize;
int toIndex   = Math.min(fromIndex + pageSize, total);
List<Product> pagedProducts = fromIndex < total
        ? new ArrayList<>(products.subList(fromIndex, toIndex))
        : List.of();

// ── Tags for sidebar ───────────────────────────────────
List<String> allTags = productRepository.findAll().stream()
    .filter(p -> p.getTags() != null && !p.getTags().isBlank())
    .flatMap(p -> Arrays.stream(p.getTags().split(",")))
    .map(String::trim)
    .filter(t -> !t.isEmpty())
    .distinct()
    .sorted()
    .collect(Collectors.toList());

// ── Cart count ─────────────────────────────────────────
if (userDetails != null) {
    model.addAttribute("cartCount",
        cartService.getCartCount(userDetails.getUsername()));
} else {
    model.addAttribute("cartCount", 0);
}

model.addAttribute("products",         pagedProducts);
model.addAttribute("categories",       categoryRepository.findAll());
model.addAttribute("selectedCategory", categoryF);
model.addAttribute("selectedRating",   rating);
model.addAttribute("maxPrice",         maxPrice);
model.addAttribute("sort",             sortF);
model.addAttribute("totalProducts",    total);
model.addAttribute("allTags",          allTags);
model.addAttribute("activeTag",        tagF);
model.addAttribute("search",           searchF);
model.addAttribute("gender",           genderF);
model.addAttribute("currentPage",      page);
model.addAttribute("totalPages",       (int) Math.ceil((double) total / pageSize));

model.addAttribute("currentUri", "/shop");
return "shop";
}



@ModelAttribute("currentUser")
public User currentUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return null;
    return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
}


}