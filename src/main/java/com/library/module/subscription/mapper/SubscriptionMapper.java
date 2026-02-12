package com.library.module.subscription.mapper;

import com.library.module.subscription.dto.SubscriptionDTO;
import com.library.module.subscription.exception.PlanNotFoundException;
import com.library.module.subscription.model.Subscription;
import com.library.module.subscription.model.SubscriptionPlan;
import com.library.module.subscription.repository.SubscriptionPlanRepository;
import com.library.module.user.model.User;
import com.library.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;

    /**
     * Convert Subscription entity to DTO
     *
     * @param subscription
     * @return
     */
    public SubscriptionDTO toDTO(Subscription subscription) {
        if (subscription == null) return null;

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(dto.getId());

        // User information
        if (subscription.getUser() != null) {
            dto.setId(subscription.getUser().getId());
            dto.setUserName(subscription.getUser().getFullName());
            dto.setUserEmail(subscription.getUser().getEmail());
        }

        // Plan information
        if (subscription.getPlan() != null) {
            dto.setId(subscription.getPlan().getId());
        }

        dto.setPlanName(subscription.getPlanName());
        dto.setPlanCode(subscription.getPlanCode());
        dto.setPrice(subscription.getPrice());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setIsActive(subscription.getIsActive());
        dto.setMaxBooksAllowed(subscription.getMaxBooksAllowed());
        dto.setMaxDaysPerBook(subscription.getMaxDaysPerBook());
        dto.setAutoRenew(subscription.getAutoRenew());
        dto.setCancelledAt(subscription.getCancelledAt());
        dto.setCancellationReason(subscription.getCancellationReason());
        dto.setNotes(subscription.getNotes());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());

        // Calculated fields
        dto.setDaysRemaining(subscription.getDaysRemaining());
        dto.setIsValid(subscription.isValid());
        dto.setIsExpired(subscription.isExpired());

        return dto;
    }

    /**
     * Convert SubscriptionDTO to entity
     *
     * @param dto
     * @return
     */
    public Subscription toEntity(SubscriptionDTO dto,SubscriptionPlan plan,User user) {
        if (dto == null) return null;

        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());

        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setNotes(dto.getNotes());
        
        return subscription;
    }

    /**
     * Convert list of subscriptions to DTOs
     */
    public List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(this::toDTO)
                .toList();
    }
}
