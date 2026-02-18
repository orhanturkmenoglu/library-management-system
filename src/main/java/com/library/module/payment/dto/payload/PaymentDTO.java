package com.library.module.payment.dto.payload;

import com.library.module.payment.enums.PaymentGateway;
import com.library.module.payment.enums.PaymentStatus;
import com.library.module.payment.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;

    private Long userId;

    private String userName;

    private Long bookLoanId;

    private Long subscriptionId;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private PaymentGateway paymentGateway;

    private Long amount;

    private String transactionId;

    private String gatewayPaymentId;

    private String gatewayOrderId;

    private String gatewaySignature;

    private String description;

    private String failureReason;

    private Integer retryCount;

    private LocalDateTime initiatedAt;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
