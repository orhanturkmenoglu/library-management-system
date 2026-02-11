package com.library.module.subscription.controller;

import com.library.module.subscription.dto.SubscriptionPlanDTO;
import com.library.module.subscription.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription-plans")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/admin/create")
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(
           @Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) {

        log.info("Create Subscription Plan request: {}", subscriptionPlanDTO);
        SubscriptionPlanDTO createdPlan =
                subscriptionPlanService.createSubscriptionPlan(subscriptionPlanDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }

    @PutMapping("/admin/{planId}")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(
            @PathVariable Long planId,
            @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) {

        log.info("Update Subscription Plan request - planId: {}, body: {}", planId, subscriptionPlanDTO);
        SubscriptionPlanDTO updatedPlan =
                subscriptionPlanService.updateSubscriptionPlan(planId, subscriptionPlanDTO);

        return ResponseEntity.ok(updatedPlan);
    }

    @DeleteMapping("/admin/{planId}")
    public ResponseEntity<Void> deleteSubscriptionPlan(@PathVariable Long planId) {

        log.info("Delete Subscription Plan request - planId: {}", planId);
        subscriptionPlanService.deleteSubscriptionPlan(planId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>> getSubscriptionPlans() {

        log.info("Get all Subscription Plans request");
        List<SubscriptionPlanDTO> plans =
                subscriptionPlanService.getSubscriptionPlans();

        return ResponseEntity.ok(plans);
    }
}
