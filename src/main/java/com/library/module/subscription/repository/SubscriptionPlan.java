package com.library.module.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlan extends JpaRepository<SubscriptionPlan,Long> {
}
