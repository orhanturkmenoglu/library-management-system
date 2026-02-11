package com.library.module.subscription.service.impl;

import com.library.module.subscription.dto.SubscriptionPlanDTO;
import com.library.module.subscription.service.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        return null;
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO subscriptionPlanDTO) {
        return null;
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) {

    }

    @Override
    public List<SubscriptionPlanDTO> getSubscriptionPlans() {
        return List.of();
    }
}
