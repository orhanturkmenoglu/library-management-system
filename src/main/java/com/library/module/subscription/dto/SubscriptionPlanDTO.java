package com.library.module.subscription.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record SubscriptionPlanDTO(
        Long id,

        @NotBlank(message = "Plan code cannot be empty")
        @Size(max = 50)
        String planCode,

        @NotBlank(message = "Plan name cannot be empty")
        @Size(max = 100)
        String name,

        @Size(max = 500)
        String description,

        @NotNull(message = "Duration days is required")
        @Positive(message = "Duration days must be positive")
        Integer durationDays,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        BigDecimal price,

        @Size(min = 3, max = 3, message = "Currency must be 3 characters (ISO)")
        String currency,

        @NotNull(message = "Max books allowed is required")
        @Positive(message = "Max books must be positive")
        Integer maxBooksAllowed,

        @NotNull(message = "Max days per book is required")
        @Positive(message = "Max days per book must be positive")
        Integer maxDaysPerBook,

        @PositiveOrZero(message = "Display order must be zero or positive")
        Integer displayOrder,

        Boolean isActive,
        Boolean isFeatured,

        @Size(max = 50)
        String badgeText,

        @Size(max = 500)
        String adminNotes,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy) {
}
