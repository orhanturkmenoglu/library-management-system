package com.library.module.subscription.repository;

import com.library.module.subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {


    @Query("""
            SELECT s FROM Subscription s WHERE s.id=:userId AND  s.isActive = true 
                         AND s.startDate<=:today and s.endDate>=:today
            """)
    Optional<Subscription> findActiveSubscriptionByUserId(@Param("userId") Long userId,
                                                          @Param("today")LocalDate today);

    @Query(
            """
            SELECT  s FROM Subscription s where s.isActive=true 
                        AND s.endDate<:today
            """
    )
    List<Subscription> findExpiredActiveSubscription(@Param("today") LocalDate today);

}
