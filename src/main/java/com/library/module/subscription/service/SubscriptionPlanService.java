package com.library.module.subscription.service;

import com.library.module.subscription.dto.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {

    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO);

    SubscriptionPlanDTO updateSubscriptionPlan (Long planId,SubscriptionPlanDTO subscriptionPlanDTO);

    void deleteSubscriptionPlan(Long planId);

    List<SubscriptionPlanDTO> getSubscriptionPlans();

}
