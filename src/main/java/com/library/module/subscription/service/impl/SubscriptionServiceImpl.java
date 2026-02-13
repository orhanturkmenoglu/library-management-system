package com.library.module.subscription.service.impl;

import com.library.module.auth.mapper.UserMapper;
import com.library.module.subscription.dto.SubscriptionDTO;
import com.library.module.subscription.exception.PlanNotFoundException;
import com.library.module.subscription.exception.SubscriptionException;
import com.library.module.subscription.exception.SubscriptionNotFoundException;
import com.library.module.subscription.mapper.SubscriptionMapper;
import com.library.module.subscription.model.Subscription;
import com.library.module.subscription.model.SubscriptionPlan;
import com.library.module.subscription.repository.SubscriptionPlanRepository;
import com.library.module.subscription.repository.SubscriptionRepository;
import com.library.module.subscription.service.SubscriptionService;
import com.library.module.user.dto.UserDTO;
import com.library.module.user.model.User;
import com.library.module.user.repository.UserRepository;
import com.library.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public SubscriptionDTO subscribe(SubscriptionDTO dto) {
        log.info("SubscriptionServiceImpl::subscribe started...");

        UserDTO currentUserDTO = userService.getCurrentUser();

        User user = userRepository.findById(currentUserDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SubscriptionPlan plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new PlanNotFoundException("Plan Id not found."));

        Subscription subscription = subscriptionMapper.toEntity(dto, plan, user);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // create payment (todo )

        return subscriptionMapper.toDTO(savedSubscription);
    }

    @Override
    public SubscriptionDTO getUsersActiveSubscription(Long userId) {
        UserDTO currentUserDTO = userService.getCurrentUser();

        Subscription subscription = subscriptionRepository.
                findActiveSubscriptionByUserId(currentUserDTO.getId(), LocalDate.now())
                .orElseThrow(() -> new SubscriptionNotFoundException("No active subscription found!"));

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) {

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found!"));

        if (!subscription.getIsActive()) {
            throw new SubscriptionException("Subscription is already inactive");
        }

        // Mark as cancelled
        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason : "Cancelled by user");

        subscription = subscriptionRepository.save(subscription);

        log.info("Subscription cancelled successfully : {}", subscriptionId);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO activeSubscription(Long subscriptionId, Long paymentId) {
        log.info("SubscriptionServiceImpl::activeSubscription started...");

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found!"));

        // verify payment

        subscription.setIsActive(true);
        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        return subscriptionRepository.findAll()
                .stream()
                .map(subscriptionMapper::toDTO)
                .toList();
    }

    @Override
    public void deactivateExpiredSubscription() {
        List<Subscription> expiredActiveSubscription =
                subscriptionRepository.findExpiredActiveSubscription(LocalDate.now());

        for (Subscription subscription : expiredActiveSubscription) {
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }
}
