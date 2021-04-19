package org.tds.trial.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.tds.trial.domain.TdsUser;

/**
 * Spring Data SQL repository for the TdsUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TdsUserRepository extends JpaRepository<TdsUser, String> {}
