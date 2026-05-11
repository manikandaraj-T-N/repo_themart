package com.themart.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.themart.model.CartItem;
import com.themart.model.Order;
import com.themart.model.OrderItem;
import com.themart.model.Product;
import com.themart.model.User;
import com.themart.repository.OrderRepository;
import com.themart.repository.ProductRepository;
import com.themart.repository.UserRepository;
import com.themart.service.CartService;
import com.themart.service.EmailService;
import com.themart.service.RazorpayService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final RazorpayService razorpayService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    private static final BigDecimal FREE_LIMIT   = new BigDecimal("55");
    private static final BigDecimal DELIVERY_FEE = new BigDecimal("5");

    // =========================
    // CHECKOUT PAGE
    // =========================
    @GetMapping("/checkout")
    public String checkoutPage(@AuthenticationPrincipal UserDetails userDetails,
                               Model model) {

        if (userDetails == null) return "redirect:/login";

        User user = getUser(userDetails);
        List<CartItem> cartItems = cartService.getCartItems(user.getEmail());

        if (cartItems.isEmpty()) return "redirect:/cart";

        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal delivery = calculateDelivery(subtotal);
        BigDecimal total    = subtotal.add(delivery);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("delivery", delivery);
        model.addAttribute("total", total);
        model.addAttribute("razorpayKeyId", razorpayKeyId);

        return "checkout";
    }

    // =========================
    // COD / NORMAL CHECKOUT
    // =========================
    @PostMapping("/checkout")
    @Transactional
    public String processCheckout(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam String shippingName,
                                  @RequestParam String shippingPhone,
                                  @RequestParam String shippingAddress,
                                  @RequestParam String shippingCity,
                                  @RequestParam String shippingPincode,
                                  @RequestParam String paymentMethod,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        if (userDetails == null) return "redirect:/login";

        try {
            User user = getUser(userDetails);
            List<CartItem> cartItems = cartService.getCartItems(user.getEmail());

            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty!");
                return "redirect:/cart";
            }

            Order order = createOrder(user, cartItems,
                    shippingName, shippingPhone, shippingAddress,
                    shippingCity, shippingPincode, paymentMethod);

            // COD flow
            if ("COD".equals(paymentMethod)) {
                postOrderSuccess(order, userDetails);
                redirectAttributes.addFlashAttribute("successMessage",
                        "Order placed! Order ID: #" + order.getOrderNumber());
                return "redirect:/order/success/" + order.getId();
            }

            // ONLINE PAYMENT
            JSONObject razorpayOrder = razorpayService.createOrder(
                    order.getTotalAmount(), order.getOrderNumber());

            model.addAttribute("order", order);
            model.addAttribute("razorpayOrderId", razorpayOrder.getString("id"));
            model.addAttribute("razorpayKeyId", razorpayKeyId);
            model.addAttribute("amount", order.getTotalAmount()
                    .multiply(new BigDecimal("100")).intValue());

            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userName", shippingName);
            model.addAttribute("userPhone", shippingPhone);

            return "payment";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Order failed: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    // =========================
    // AJAX PAYMENT INIT
    // =========================
    @PostMapping("/checkout/initiate")
    @ResponseBody
    @Transactional
    public Map<String, Object> initiatePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String shippingName,
            @RequestParam String shippingPhone,
            @RequestParam String shippingAddress,
            @RequestParam String shippingCity,
            @RequestParam String shippingPincode,
            @RequestParam String paymentMethod) {

        try {
            User user = getUser(userDetails);
            List<CartItem> cartItems = cartService.getCartItems(user.getEmail());

            if (cartItems.isEmpty()) return Map.of("error", "Cart is empty");

            Order order = createOrder(user, cartItems,
                    shippingName, shippingPhone, shippingAddress,
                    shippingCity, shippingPincode, paymentMethod);

            JSONObject rzpOrder = razorpayService.createOrder(
                    order.getTotalAmount(), order.getOrderNumber());

            return Map.of(
                    "orderId", order.getId(),
                    "orderNumber", order.getOrderNumber(),
                    "razorpayOrderId", rzpOrder.getString("id"),
                    "amount", order.getTotalAmount()
                            .multiply(new BigDecimal("100")).intValue()
            );

        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    // =========================
    // PAYMENT VERIFY
    // =========================
    @PostMapping("/payment/verify")
    @Transactional
    public String verifyPayment(@RequestParam String razorpay_order_id,
                                @RequestParam String razorpay_payment_id,
                                @RequestParam String razorpay_signature,
                                @RequestParam Long orderId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean valid = razorpayService.verifySignature(
                razorpay_order_id, razorpay_payment_id, razorpay_signature);

        if (valid) {
            order.setPaymentStatus(Order.PaymentStatus.PAID);
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);

            postOrderSuccess(order, userDetails);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Payment successful! Order #" + order.getOrderNumber());

            return "redirect:/order/success/" + order.getId();
        }

        order.setPaymentStatus(Order.PaymentStatus.FAILED);
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("errorMessage",
                "Payment verification failed.");
        return "redirect:/cart";
    }

    // =========================
    // SUCCESS PAGE
    // =========================
    @GetMapping("/order/success/{id}")
    public String orderSuccess(@PathVariable Long id, Model model) {
        orderRepository.findById(id).ifPresent(order -> {
            order.getOrderItems().size();
            model.addAttribute("order", order);
        });
        return "order-success";
    }

    // =========================
    // 🔹 HELPER METHODS
    // =========================

    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private BigDecimal calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(ci -> ci.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDelivery(BigDecimal subtotal) {
        return subtotal.compareTo(FREE_LIMIT) >= 0
                ? BigDecimal.ZERO
                : DELIVERY_FEE;
    }

    private Order createOrder(User user,
                              List<CartItem> cartItems,
                              String name, String phone,
                              String address, String city, String pincode,
                              String paymentMethod) {

        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal delivery = calculateDelivery(subtotal);
        BigDecimal total    = subtotal.add(delivery);

        Order order = Order.builder()
                .user(user)
                .totalAmount(total)
                .deliveryCharge(delivery)
                .discountAmount(BigDecimal.ZERO)
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .orderNumber("ORD-" + System.currentTimeMillis())
                .paymentMethod(paymentMethod)
                .shippingName(name)
                .shippingAddress(address)
                .shippingCity(city)
                .shippingPincode(pincode)
                .shippingPhone(phone)
                .build();

        List<OrderItem> items = cartItems.stream().map(ci -> {
            BigDecimal unit = ci.getProduct().getPrice();
            return OrderItem.builder()
                    .order(order)
                    .product(ci.getProduct())
                    .quantity(ci.getQuantity())
                    .unitPrice(unit)
                    .totalPrice(unit.multiply(BigDecimal.valueOf(ci.getQuantity())))
                    .build();
        }).toList();

        order.setOrderItems(items);
        orderRepository.save(order);

        return order;
    }

    private void postOrderSuccess(Order order, UserDetails userDetails) {

        // clear cart
        cartService.clearCart(userDetails.getUsername());

        // reduce stock
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int newStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(Math.max(0, newStock));
            productRepository.save(product);
        }

        // send email (non-blocking safe)
        try {
            emailService.sendOrderConfirmation(order.getUser().getEmail(), order);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }

    @ModelAttribute("currentUser")
public User currentUser(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) return null;
    return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
}
}