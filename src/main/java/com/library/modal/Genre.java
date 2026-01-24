package com.library.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Book Category
public class Genre  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Genre code is mandatory")
    private String code;

    @NotBlank(message = "Genre name is mandatory")
    private String name;

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    @Min(value = 0, message = "Display order must be non-negative")
    private Integer displayOrder = 0;

    @Column(nullable = false)
    private Boolean active = true;


    @ManyToOne
    private Genre parentGenre; // bir Genre bir üst genre'a sahip olabilir

    @OneToMany
    private List<Genre> subGenres = new ArrayList<>(); // bir Genre'ın birden fazla alt genre'ı olabilir

    /*@OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST)
    private List<Book> books = new ArrayList<>();
    */

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
