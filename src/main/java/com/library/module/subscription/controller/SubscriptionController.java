package com.library.module.subscription.controller;

import com.library.module.subscription.dto.SubscriptionDTO;
import com.library.module.subscription.service.SubscriptionService;
import com.library.shared.payload.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Subscribe user to a plan
     */
    @PostMapping
    public ResponseEntity<SubscriptionDTO> subscribe(
            @Valid @RequestBody SubscriptionDTO subscriptionDTO) {

        log.info("Subscribe request received");
        SubscriptionDTO response = subscriptionService.subscribe(subscriptionDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get current user's active subscription
     */
    @GetMapping("/user/active")
    public ResponseEntity<SubscriptionDTO> getUsersActiveSubscription(
            @RequestParam(value = "userId",required = false) Long userId) {

        log.info("Get active subscription request");
        SubscriptionDTO subscription =
                subscriptionService.getUsersActiveSubscription(userId);

        return ResponseEntity.ok(subscription);
    }

    /**
     * Activate subscription after payment success
     */
    @PostMapping("/{subscriptionId}/activate")
    public ResponseEntity<SubscriptionDTO> activateSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam Long paymentId) {

        log.info("Activate subscription request - subscriptionId: {}", subscriptionId);
        SubscriptionDTO subscription =
                subscriptionService.activeSubscription(subscriptionId, paymentId);

        return ResponseEntity.ok(subscription);
    }

    /**
     * Cancel subscription
     */
    @PostMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam(required = false) String reason) {

        log.info("Cancel subscription request - subscriptionId: {}", subscriptionId);
        SubscriptionDTO subscription =
                subscriptionService.cancelSubscription(subscriptionId, reason);

        return ResponseEntity.ok(subscription);
    }

    /**
     * Get all subscriptions ADMIN
     */
    @GetMapping("/admin")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        log.info("Get all subscriptions request");
        List<SubscriptionDTO> subscriptions =
                subscriptionService.getAllSubscriptions(pageable);

        return ResponseEntity.ok(subscriptions);
    }

    /**
     *  Admin deactivate expired user only
     */
    @GetMapping("/admin/deactivate-expired/{userId}")
    public ResponseEntity<?> deactivateExpiredSubscriptions(@PathVariable("userId")
                                                            Long userId) {
        log.info("Deactivate expired subscription request");

        subscriptionService.deactivateExpiredSubscription();
        ApiResponse apiResponse = new ApiResponse("task done !", true);
        return ResponseEntity.ok(apiResponse);
    }
}
