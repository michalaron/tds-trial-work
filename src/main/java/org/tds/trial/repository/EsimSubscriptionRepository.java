package org.tds.trial.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.tds.trial.domain.EsimSubscription;

/**
 * Spring Data SQL repository for the EsimSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsimSubscriptionRepository extends JpaRepository<EsimSubscription, Long> {}
