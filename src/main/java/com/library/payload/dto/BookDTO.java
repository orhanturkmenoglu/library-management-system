package com.library.payload.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {


    private Long id;

    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1,max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 1,max = 255, message = "Author name must be between 1 and 255 characters")
    private String author;

    private Long genreId;

    private String genreName;

    private String genreCode;

    @Size(max = 100, message = "Publisher name must be less than 100 characters")
    private String publisher;

    private LocalDate publishedDate;

    @Size(max = 20,message = "Language must be less than 20 characters")
    private String language;

    @Min(value = 1, message = "Pages must be at least 1")
    @Max(value =50000, message = "Pages must be less than 50000")
    private Integer pages;

    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;

    @Min(value = 0, message = "Total copies cannot be negative")
    @NotNull(message = "Total copies cannot be null")
    private Integer totalCopies;

    @Min(value = 0, message = "Available copies cannot be negative")
    @NotNull(message = "Available copies cannot be null")
    private Integer availableCopies;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Price must be a valid monetary amount")
    private BigDecimal price;

    @Size(max = 500, message = "Cover image URL must be less than 500 characters")
    private String coverImageUrl;


    private Boolean alreadyHaveLoan;

    private Boolean alreadyHaveReservation;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
