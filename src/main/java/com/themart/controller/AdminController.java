package com.themart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.themart.model.Blog;
import com.themart.model.Category;
import com.themart.model.Product;
import com.themart.model.User;
import com.themart.repository.BlogRepository;
import com.themart.repository.CategoryRepository;
import com.themart.repository.ProductRepository;
import com.themart.repository.UserRepository;
import com.themart.repository.WishlistRepository;
import com.themart.repository.NewsletterSubscriberRepository;
import com.themart.service.CartService;
import com.themart.service.ProductService;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final NewsletterSubscriberRepository subscriberRepository;
    private final BlogRepository blogRepository;
    private final WishlistRepository wishlistRepository;



    // ─── Dashboard ───────────────────────────────────────
   @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProducts",   productRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("featuredCount",   productRepository.findByIsFeaturedTrue().size());
        // model.addAttribute("lowStockCount",   productRepository.findByStockQuantityLessThan(10).size());
        // model.addAttribute("recentProducts",  productRepository.findTop5ByOrderByCreatedAtDesc());
        return "admin/dashboard";
    }



@GetMapping("/newsletter")
public String newsletterSubscribers(Model model) {
    model.addAttribute("subscribers",
        subscriberRepository.findAll(Sort.by(Sort.Direction.DESC, "subscribedAt")));
    return "admin/newsletter";
}

@PostMapping("/newsletter/delete/{id}")
public String deleteSubscriber(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
    subscriberRepository.deleteById(id);
    redirectAttributes.addFlashAttribute("successMessage", "Subscriber removed.");
    return "redirect:/admin/newsletter";
}




@ModelAttribute("cartCount")
public int cartCount(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return 0;
    return cartService.getCartCount(userDetails.getUsername());
}

@ModelAttribute("wishlistCount")
public long wishlistCount(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return 0;
    return wishlistRepository.countByUserEmail(userDetails.getUsername());
}


@ModelAttribute("currentUser")
public User currentUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return null;
    return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
}
}