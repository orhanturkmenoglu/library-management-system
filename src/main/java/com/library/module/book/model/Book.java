package com.library.module.book.model;

import com.library.module.genre.model.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Genre genre;

    private String publisher;

    private LocalDate publishedDate;

    private String language;

    private Integer pages;

    private String description;

    @Column(nullable = false)
    private Integer totalCopies;

    private BigDecimal price;

    @Column(nullable = false)
    private Integer availableCopies;

    private String coverImageUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @AssertTrue(message = "Available copies cannot exceed total copies")
    public boolean isAvailableCopiesValid() {
        if (totalCopies == null || availableCopies == null) {
            return true;
        }
        return availableCopies <= totalCopies;
    }
}
