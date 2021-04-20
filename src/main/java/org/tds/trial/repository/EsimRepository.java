package org.tds.trial.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.tds.trial.domain.Esim;

/**
 * Spring Data SQL repository for the Esim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsimRepository extends JpaRepository<Esim, Long> {}
