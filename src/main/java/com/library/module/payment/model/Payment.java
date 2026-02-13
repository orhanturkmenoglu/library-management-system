package com.library.module.payment.model;

import com.library.module.payment.enums.PaymentGateway;
import com.library.module.payment.enums.PaymentStatus;
import com.library.module.payment.enums.PaymentType;
import com.library.module.subscription.model.Subscription;
import com.library.module.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subscription subscription;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(value = EnumType.STRING)
    private PaymentGateway paymentGateway;

    private Long amount;

    private String transactionId;

    private String gatewayOrderId;

    private String gatewaySignature;

    private String description;

    private String failureReason;

    @CreationTimestamp
    private LocalDateTime initiatedAt;

    private LocalDateTime completedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

