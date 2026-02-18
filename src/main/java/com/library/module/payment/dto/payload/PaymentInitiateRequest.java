package com.library.module.payment.dto.payload;

import com.library.module.payment.enums.PaymentGateway;
import com.library.module.payment.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiateRequest {

    @NotNull(message = "User ID must not be null.")
    private Long userId;

    @NotNull(message = "Payment type must be specified.")
    private PaymentType paymentType;

    @NotNull(message = "Payment gateway must be specified.")
    private PaymentGateway paymentGateway;

    @NotNull(message = "Amount must not be null.")
    @Positive(message = "Amount must be greater than 0.")
    private Long amount;

    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description;

    private Long fineId;

    private Long subscriptionId;

    @Size(max = 500, message = "Success URL must not exceed 500 characters.")
    private String successUrl;

    @Size(max = 500, message = "Cancel URL must not exceed 500 characters.")
    private String cancelUrl;
}
