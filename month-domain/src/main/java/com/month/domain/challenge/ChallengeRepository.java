package com.month.domain.challenge;

import com.month.domain.challenge.repository.ChallengeRepositoryCustom;
import com.month.domain.common.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom {
    Challenge findByUuid(Uuid uuid);
}
