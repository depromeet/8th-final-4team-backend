package com.month.domain.challenge;

import com.month.domain.challenge.repository.ChallengePlanRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengePlanRepository extends JpaRepository<ChallengePlan, Long>, ChallengePlanRepositoryCustom {

}
