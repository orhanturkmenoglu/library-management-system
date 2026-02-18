package com.library.module.payment.service.impl;

import com.library.module.payment.dto.payload.PaymentDTO;
import com.library.module.payment.dto.payload.PaymentInitiateRequest;
import com.library.module.payment.dto.request.PaymentVerifyRequest;
import com.library.module.payment.dto.response.PaymentInitiateResponse;
import com.library.module.payment.enums.PaymentStatus;
import com.library.module.payment.model.Payment;
import com.library.module.payment.repository.PaymentRepository;
import com.library.module.payment.service.PaymentService;
import com.library.module.subscription.exception.SubscriptionNotFoundException;
import com.library.module.subscription.model.Subscription;
import com.library.module.subscription.repository.SubscriptionRepository;
import com.library.module.user.model.User;
import com.library.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found :" + request.getUserId()));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentType(request.getPaymentType());
        payment.setPaymentGateway(request.getPaymentGateway());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().replace("-", "").toUpperCase());
        payment.setInitiatedAt(LocalDateTime.now(ZoneOffset.UTC));

        if(request.getSubscriptionId() !=null ){
            Subscription subscription = subscriptionRepository
                    .findById(request.getSubscriptionId())
                    .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found :" + request.getSubscriptionId()));

            payment.setSubscription(subscription);
        }

        payment = paymentRepository.save(payment);
        return null;
    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest verifyRequest) {
        return null;
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return null;
    }
}
