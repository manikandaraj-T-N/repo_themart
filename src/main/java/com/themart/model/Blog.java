package com.themart.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String author;
    private String imageUrl;
    private String tags;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isPublished;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}