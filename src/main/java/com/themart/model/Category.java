package com.themart.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter          
@Setter          
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private String imageUrl;
    private String slug;

    @Column(name = "is_active")
    private Boolean isActive = true;


    @ToString.Exclude
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;



    public String getSlugDisplay() {
    if (slug == null || slug.isBlank())
        return name != null ? name.toUpperCase() : "UNCATEGORIZED";
    return slug.replace("-", " ").toUpperCase();
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "parent_id")
private Category parent;

@Column(name = "gender")
private String gender; // "mens", "womens", "children", "unisex"

}