package com.themart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "image_url")
    private String imageUrl;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_organic")
    private Boolean isOrganic = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "discount_percentage")
    private Integer discountPercentage = 0;

    private String unit; // e.g., "kg", "piece", "dozen"

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "short_description")
    private String shortDescription;

    @PrePersist
    protected void onCreate() {
       if (this.createdAt == null) {
        this.createdAt = LocalDateTime.now();
    }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;


public boolean isInStock() {
    return stockQuantity != null && stockQuantity > 0;
}

@Column(name = "tags")
private String tags;

@Column(name = "gender")
private String gender;

}
