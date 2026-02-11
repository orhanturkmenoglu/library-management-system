package com.library.module.subscription.service.impl;

import com.library.module.subscription.dto.SubscriptionPlanDTO;
import com.library.module.subscription.exception.PlanAlreadyExistsException;
import com.library.module.subscription.mapper.SubscriptionPlanMapper;
import com.library.module.subscription.model.SubscriptionPlan;
import com.library.module.subscription.repository.SubscriptionPlanRepository;
import com.library.module.subscription.service.SubscriptionPlanService;
import com.library.module.user.dto.UserDTO;
import com.library.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;
    private final UserService userService;

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO subscriptionPlanDTO) {
        Boolean existsByPlanCode = subscriptionPlanRepository.existsByPlanCode(subscriptionPlanDTO.planCode());

        if (existsByPlanCode) {
            throw new PlanAlreadyExistsException("Plan code is already exist");
        }

        SubscriptionPlan plan = subscriptionPlanMapper.toEntity(subscriptionPlanDTO);
        log.info("MapToEntity Plan: {}", plan);

        UserDTO currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName());

        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        log.info("SavedPlan : {}", savedPlan);
        return subscriptionPlanMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO subscriptionPlanDTO) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan id not found"));

        log.info("Plan: {}", plan);

        subscriptionPlanMapper.updateEntity(plan, subscriptionPlanDTO);

        UserDTO currentUser = userService.getCurrentUser();
        plan.setUpdatedBy(currentUser.getFullName());
        SubscriptionPlan updatedSubscriptionPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toDTO(updatedSubscriptionPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan id not found"));

        subscriptionPlanRepository.delete(plan);
    }

    @Override
    public List<SubscriptionPlanDTO> getSubscriptionPlans() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .map(subscriptionPlanMapper::toDTO)
                .toList();
    }
}
