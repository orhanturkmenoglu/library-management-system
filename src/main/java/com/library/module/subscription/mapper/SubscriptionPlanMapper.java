package com.library.module.subscription.mapper;

import com.library.module.subscription.dto.SubscriptionPlanDTO;
import com.library.module.subscription.model.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlanDTO toDTO (SubscriptionPlan subscriptionPlan){
        if(subscriptionPlan == null){
            return null;
        }

        return  SubscriptionPlanDTO.builder()
                .id(subscriptionPlan.getId())
                .planCode(subscriptionPlan.getPlanCode())
                .name(subscriptionPlan.getName())
                .description(subscriptionPlan.getDescription())
                .durationDays(subscriptionPlan.getDurationDays())
                .price(subscriptionPlan.getPrice())
                .currency(subscriptionPlan.getCurrency())
                .maxBooksAllowed(subscriptionPlan.getMaxBooksAllowed())
                .maxDaysPerBook(subscriptionPlan.getMaxDaysPerBook())
                .displayOrder(subscriptionPlan.getDisplayOrder())
                .isActive(subscriptionPlan.getIsActive())
                .isFeatured(subscriptionPlan.getIsFeatured())
                .badgeText(subscriptionPlan.getBadgeText())
                .adminNotes(subscriptionPlan.getAdminNotes())
                .createdAt(subscriptionPlan.getCreatedAt())
                .updatedAt(subscriptionPlan.getUpdatedAt())
                .createdBy(subscriptionPlan.getCreatedBy())
                .updatedBy(subscriptionPlan.getUpdatedBy())
                .build();
    }

    public SubscriptionPlan toEntity (SubscriptionPlanDTO subscriptionPlanDTO){
        if (subscriptionPlanDTO == null){
            return null;
        }

        return SubscriptionPlan.builder()
                .id(subscriptionPlanDTO.id())
                .planCode(subscriptionPlanDTO.planCode())
                .name(subscriptionPlanDTO.name())
                .description(subscriptionPlanDTO.description())
                .durationDays(subscriptionPlanDTO.durationDays())
                .price(subscriptionPlanDTO.price())
                .currency(subscriptionPlanDTO.currency() !=null ? subscriptionPlanDTO.currency() : "INR")
                .maxBooksAllowed(subscriptionPlanDTO.maxBooksAllowed())
                .maxDaysPerBook(subscriptionPlanDTO.maxDaysPerBook())
                .displayOrder(subscriptionPlanDTO.displayOrder() != null ? subscriptionPlanDTO.displayOrder() : 0)
                .isActive(subscriptionPlanDTO.isActive() !=null ? subscriptionPlanDTO.isActive() : true)
                .isFeatured(subscriptionPlanDTO.isFeatured() !=null ? subscriptionPlanDTO.isFeatured() : false)
                .badgeText(subscriptionPlanDTO.badgeText())
                .adminNotes(subscriptionPlanDTO.adminNotes())
                .createdBy(subscriptionPlanDTO.createdBy())
                .updatedBy(subscriptionPlanDTO.updatedBy())
                .build();
    }

    public void updateEntity (SubscriptionPlan subscriptionPlan, SubscriptionPlanDTO dto){
        if (subscriptionPlan == null || dto == null){
            return;
        }

        if(dto.name() != null) {
            subscriptionPlan.setName(dto.name());
        }

        if(dto.description() != null) {
            subscriptionPlan.setDescription(dto.description());
        }

        if(dto.durationDays() != null) {
            subscriptionPlan.setDurationDays(dto.durationDays());
        }

        if(dto.price() != null) {
            subscriptionPlan.setPrice(dto.price());
        }
        if(dto.currency() != null) {
            subscriptionPlan.setCurrency(dto.currency());
        }

        if(dto.maxBooksAllowed() != null) {
            subscriptionPlan.setMaxBooksAllowed(dto.maxBooksAllowed());
        }

        if(dto.maxDaysPerBook() != null) {
            subscriptionPlan.setMaxDaysPerBook(dto.maxDaysPerBook());
        }
        if(dto.displayOrder() != null) {
            subscriptionPlan.setDisplayOrder(dto.displayOrder());
        }
        if(dto.isActive() != null) {
            subscriptionPlan.setIsActive(dto.isActive());
        }
        if(dto.isFeatured() != null) {
            subscriptionPlan.setIsFeatured(dto.isFeatured());
        }
        if(dto.badgeText() != null) {
            subscriptionPlan.setBadgeText(dto.badgeText());
        }
        if(dto.adminNotes() != null) {
            subscriptionPlan.setAdminNotes(dto.adminNotes());
        }

        if(dto.updatedBy() !=null){
            subscriptionPlan.setUpdatedBy(dto.updatedBy());
        }
    }
}
