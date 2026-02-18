package com.library.module.subscription.service;

import com.library.module.subscription.dto.SubscriptionDTO;
import com.library.module.subscription.model.SubscriptionPlan;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDTO subscribe(SubscriptionDTO dto);

    SubscriptionDTO getUsersActiveSubscription(Long userId);

    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason);

    SubscriptionDTO activeSubscription(Long subscriptionId, Long paymentId);

    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);

    void deactivateExpiredSubscription();

    SubscriptionPlan getPlanByCode(String planCode);
}
