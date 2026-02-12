package com.library.module.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {

    private Long id;

    @NotNull(message = "Subscription user id not null or blank")
    private Long userId;

    private String userName;
    private String userEmail;

    @NotNull(message = "Subscription plan id not null or blank")
    private Long planId;

    private String planName;
    private String planCode;

    private BigDecimal price;
    private String currency;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isActive;

    private Integer maxBooksAllowed;
    private Integer maxDaysPerBook;

    private Boolean autoRenew;

    private LocalDateTime cancelledAt;
    private String cancellationReason;

    private String notes;

    private Long daysRemaining;
    private Boolean isValid;
    private Boolean isExpired;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
