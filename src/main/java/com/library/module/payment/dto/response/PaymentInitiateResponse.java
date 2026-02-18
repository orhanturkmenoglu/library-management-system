package com.library.module.payment.dto.response;

import com.library.module.payment.enums.PaymentGateway;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiateResponse {

    private Long paymentId;

    private PaymentGateway paymentGateway;

    private String transactionId;

    private String razorpayOrderId;

    private Long amount;

    private String description;

    private String checkoutUrl;

    private String message;

    private Boolean success;
}
