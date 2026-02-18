package com.library.module.payment.service;

import com.library.module.payment.dto.payload.PaymentDTO;
import com.library.module.payment.dto.payload.PaymentInitiateRequest;
import com.library.module.payment.dto.request.PaymentVerifyRequest;
import com.library.module.payment.dto.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest paymentInitiateRequest);

    PaymentDTO verifyPayment(PaymentVerifyRequest verifyRequest);

    Page<PaymentDTO> getAllPayments(Pageable pageable);
}
